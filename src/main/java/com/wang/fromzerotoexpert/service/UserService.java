package com.wang.fromzerotoexpert.service;

import com.mysql.cj.util.StringUtils;
import com.wang.fromzerotoexpert.dao.UserDao;
import com.wang.fromzerotoexpert.form.UserLoginForm;
import com.wang.fromzerotoexpert.handler.ConditionException;
import com.wang.fromzerotoexpert.domain.User;
import com.wang.fromzerotoexpert.util.MD5Util;
import com.wang.fromzerotoexpert.util.TokenUtil;
import com.wang.fromzerotoexpert.util.UsernameAndPasswordLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UsernameAndPasswordLimit usernameAndPasswordLimit;
    private final Object addUserLock = new Object();

    /**
     * 添加用户 加synchronized, 分布式环境下此方法无效
     * @param user
     * @throws ConditionException
     */
    public void addUser(User user) throws ConditionException {
        String username = user.getUsername();
        String password = user.getPassword();

        limitUser(username, password);
        String salt = MD5Util.generateSalt(16);
        String md5Password = MD5Util.generateMD5Password(password, salt);
        user.setSalt(salt);
        user.setPassword(md5Password);

        //查询用户名是否重复 考虑到高并发场景，因此需要加锁，但是只能单机，分布式则不行
        synchronized (addUserLock) {
            User dbuser = userDao.getUserByUsername(username);
            if (dbuser != null) {
                throw new ConditionException("该用户名已经存在");
            }
            userDao.addUser(user);
        }

        System.out.println(user.getId());
    }


    /**
     *  分布式环境 高并发添加用户，加redis分布式锁 但是key一样，相当于串行化添加用户，效率低
     * @param user
     * @throws ConditionException
     */
    public void addUser2(User user) throws ConditionException {
        String username = user.getUsername();
        String password = user.getPassword();

        limitUser(username, password);
        String salt = MD5Util.generateSalt(16);
        String md5Password = MD5Util.generateMD5Password(password, salt);
        user.setSalt(salt);
        user.setPassword(md5Password);


        //使用分布式锁
        String userValue = UUID.randomUUID().toString(); //防止误删除 每个用户判断通过value判断是否是自己设置的锁
        Boolean result = redisTemplate.opsForValue().setIfAbsent("lock", userValue);
        redisTemplate.expire("lock", 10, TimeUnit.MILLISECONDS);

        if (Boolean.FALSE.equals(result)) {
            throw new ConditionException("正在添加用户名");
        }
        try {
            //加锁成功 可以添加
            User dbuser = userDao.getUserByUsername(username);
            if (dbuser != null) {
                throw new ConditionException("该用户已存在");
            }
            userDao.addUser(user);
        } finally {
            if (userValue.equals(redisTemplate.opsForValue().get("lock"))) {
                redisTemplate.delete("lock");
            }
            //实际上还是有问题， 原因在于 查询 和 删除不是原子操作
        }


        System.out.println(user.getId());
    }

    /**
     * 添加用户，每个用户由自己的key,只有用户名相同时key才相同，才会阻塞
     * @param user
     * @throws ConditionException
     */
    public void addUser3(User user) throws ConditionException {
        String username = user.getUsername();
        String password = user.getPassword();

        limitUser(username, password);
        String salt = MD5Util.generateSalt(16);
        String md5Password = MD5Util.generateMD5Password(password, salt);
        user.setSalt(salt);
        user.setPassword(md5Password);


        //使用分布式锁
        String userValue = UUID.randomUUID().toString(); //防止误删除 每个用户判断通过value判断是否是自己设置的锁
        String userKey = username + "lock";

        Boolean result = redisTemplate.opsForValue().setIfAbsent(userKey, userValue);
        redisTemplate.expire(userKey, 10, TimeUnit.MILLISECONDS);
        //加锁和设置过期时间不是原子操作

        if (Boolean.FALSE.equals(result)) {
            throw new ConditionException("正在添加用户名");
        }
        try {
            //加锁成功 可以添加
            User dbuser = userDao.getUserByUsername(username);
            if (dbuser != null) {
                throw new ConditionException("该用户已存在");
            }
            userDao.addUser(user);
        } finally {
            if (userValue.equals(redisTemplate.opsForValue().get(userKey))) {
                redisTemplate.delete(userKey);
            }
            //实际上还是有问题， 原因在于 查询 和 删除不是原子操作
        }


        System.out.println(user.getId());
    }


    public void limitUser(String username, String password) throws ConditionException {
        int limitUsername = usernameAndPasswordLimit.limitUsername(username);
        if (limitUsername != 0) {
            throw new ConditionException(String.valueOf(limitUsername),"用户名存在敏感词汇");
        }
        int limitPassword = usernameAndPasswordLimit.limitPassword(password);
        if (limitPassword != 0) {
            throw new ConditionException(String.valueOf(limitPassword), "密码强度不够");
        }

    }


    /**
     * 登录返回token
     * @param userLoginForm
     * @return
     * @throws Exception
     */
    public String login(UserLoginForm userLoginForm) throws Exception {
        String username = userLoginForm.getUsername();
        String rawPassword = userLoginForm.getPassword();

        if (StringUtils.isNullOrEmpty(username)) {
            throw new ConditionException("用户名不能为空");
        }
        User dbUser = userDao.getUserByUsername(username);
        if (dbUser == null) {
            throw new ConditionException("该用户不存在");
        }
        String salt = dbUser.getSalt();
        String dbmd5Password = dbUser.getPassword();

        String md5Password = MD5Util.generateMD5Password(rawPassword, salt);
        if (!md5Password.equals(dbmd5Password)) {
            throw new ConditionException("密码错误");
        }
        return TokenUtil.generateToken(dbUser.getId());
    }

    /**
     * 使用session保存登录状态
     * @return
     */
    public User login2(UserLoginForm userLoginForm) throws ConditionException {
        String username = userLoginForm.getUsername();
        String rawPassword = userLoginForm.getPassword();

        if (StringUtils.isNullOrEmpty(username)) {
            throw new ConditionException("用户名不能为空");
        }
        User dbUser = userDao.getUserByUsername(username);
        if (dbUser == null) {
            throw new ConditionException("该用户不存在");
        }
        String salt = dbUser.getSalt();
        String dbmd5Password = dbUser.getPassword();

        String md5Password = MD5Util.generateMD5Password(rawPassword, salt);
        if (!md5Password.equals(dbmd5Password)) {
            throw new ConditionException("密码错误");
        }
        return dbUser;
    }

    public User loginOnlyOne(UserLoginForm userLoginForm, HttpServletRequest request) throws ConditionException {
        User user = login2(userLoginForm);
        HttpSession session = request.getSession();
        String id = session.getId();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(id))) {
            throw new ConditionException("该用户已经登录");
        }
        redisTemplate.opsForValue().set(id, user.getUsername());
        redisTemplate.expire(id, 100000, TimeUnit.MILLISECONDS);

        session.setAttribute("currentUser", user);
        user.setPassword("");
        return user;
    }

    public User loginLimit(UserLoginForm userLoginForm, HttpServletRequest request) throws ConditionException {
        User user = login2(userLoginForm);
        HttpSession session = request.getSession();
        String id = session.getId();

        String header = request.getHeader("User-Agent");

//        if (Boolean.TRUE.equals(redisTemplate.hasKey(id))) {
//            throw new ConditionException("该用户已经登录");
//        }
//        redisTemplate.opsForValue().set(id, user.getUsername());
//        redisTemplate.expire(id, 100000, TimeUnit.MILLISECONDS);
//
//        session.setAttribute("currentUser", user);
//        user.setPassword("");
        return user;
    }
}
