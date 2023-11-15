package com.wang.fromzerotoexpert.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.wang.fromzerotoexpert.dao.UserDao;
import com.wang.fromzerotoexpert.domain.ConditionException;
import com.wang.fromzerotoexpert.domain.User;
import com.wang.fromzerotoexpert.util.UsernameAndPasswordLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
        String email = user.getEmail();
        int limitUsername = usernameAndPasswordLimit.limitUsername(username);
        if (limitUsername != 0) {
            throw new ConditionException(String.valueOf(limitUsername),"用户名存在敏感词汇");
        }
        int limitPassword = usernameAndPasswordLimit.limitPassword(password);
        if (limitPassword != 0) {
            throw new ConditionException(String.valueOf(limitPassword), "密码强度不够");
        }

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
     *  分布式环境 高并发添加用户，加redis分布式锁
     * @param user
     * @throws ConditionException
     */
    public void addUser2(User user) throws ConditionException {
        String username = user.getUsername();

        //使用分布式锁
        String userValue = UUID.randomUUID().toString(); //防止误删除 每个用户判断通过value判断是否是自己设置的锁
        redisTemplate.expire("lock", 10, TimeUnit.MILLISECONDS);
        Boolean result = redisTemplate.opsForValue().setIfAbsent("lock", userValue);
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


}
