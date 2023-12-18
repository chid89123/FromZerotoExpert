package com.wang.fromzerotoexpert.schedule;

import com.wang.fromzerotoexpert.domain.Visit;
import com.wang.fromzerotoexpert.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Component
public class RedisScheduled {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private WelcomeService welcomeService;

//    //容器启动后,延迟10秒后再执行一次定时器,以后每10秒再执行一次该定时器。
//    @Scheduled(initialDelay = 10000, fixedRate = 10000)
//    public void test() {
//        System.out.println("test");
//    }

    //容器启动后,延迟10秒后再执行一次定时器,以后每10秒再执行一次该定时器。
//    @Scheduled(initialDelay = 10000, fixedRate = 10000)

    /**
     * 每隔1000秒将1000秒之前的在线用户删除
     */
    @Scheduled(initialDelay = 1000000, fixedRate = 1000000)
    public void redischedule() {
        long timestamp = System.currentTimeMillis();
        redisTemplate.opsForZSet().removeRangeByScore("ol", -1, timestamp - 10000);
    }


    /**
     *将每日的PV IP UV刷到数据库中
     */
    // 每天的23点59点触发任务
    @Scheduled(cron = "0 59 23 * * ?")
    public void executeTask() {
        // 执行你的任务逻辑
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        String nowTime = simpleDateFormat.format(date);
        Visit visit = new Visit();
        Integer pv = Integer.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get("PV" + nowTime)));
        Integer ip = Math.toIntExact(redisTemplate.opsForHyperLogLog().size("IP" + nowTime));
        Integer uv = Math.toIntExact(redisTemplate.opsForHyperLogLog().size("UV" + nowTime));
        visit.setPv(pv);
        visit.setIp(ip);
        visit.setUv(uv);
        visit.setDate(nowTime);
        welcomeService.addVisit(visit);
        System.out.println("Scheduled task executed at 24:00 PM every day.");
    }
}


