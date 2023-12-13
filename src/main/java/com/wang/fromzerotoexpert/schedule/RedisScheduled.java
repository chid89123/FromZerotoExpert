package com.wang.fromzerotoexpert.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RedisScheduled {

    @Autowired
    private StringRedisTemplate redisTemplate;

//    //容器启动后,延迟10秒后再执行一次定时器,以后每10秒再执行一次该定时器。
//    @Scheduled(initialDelay = 10000, fixedRate = 10000)
//    public void test() {
//        System.out.println("test");
//    }

    //容器启动后,延迟10秒后再执行一次定时器,以后每10秒再执行一次该定时器。
//    @Scheduled(initialDelay = 10000, fixedRate = 10000)

    @Scheduled(initialDelay = 1000000, fixedRate = 1000000)
    public void redischedule() {
        long timestamp = System.currentTimeMillis();
        redisTemplate.opsForZSet().removeRangeByScore("ol", -1, timestamp - 10000);
    }

}


