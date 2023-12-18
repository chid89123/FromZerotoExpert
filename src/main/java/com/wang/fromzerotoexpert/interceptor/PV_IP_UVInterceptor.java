package com.wang.fromzerotoexpert.interceptor;

import com.wang.fromzerotoexpert.handler.ConditionException;
import com.wang.fromzerotoexpert.util.IpUtil;
import com.wang.fromzerotoexpert.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PV_IP_UVInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserSupport userSupport;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        String nowTime = simpleDateFormat.format(date);
        redisTemplate.opsForValue().increment("PV" + nowTime, 1);
        String ip = IpUtil.getIP(request);
        redisTemplate.opsForHyperLogLog().add("IP" + nowTime, ip);

        try {
            Long userId = userSupport.getCurrentUserId();
            redisTemplate.opsForHyperLogLog().add("UV" + nowTime, String.valueOf(userId));
        } catch (ConditionException e) {
            System.out.println("用户未登录,无法统计UV");

        }
        return true;

    }
}
