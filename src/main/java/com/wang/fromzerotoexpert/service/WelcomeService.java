package com.wang.fromzerotoexpert.service;

import com.wang.fromzerotoexpert.dao.VisitDao;
import com.wang.fromzerotoexpert.domain.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WelcomeService {
    @Autowired
    private VisitDao visitDao;

    @Autowired
    private StringRedisTemplate redisTemplate;


    public Map<String, Integer> getTodayPVAndIPAndUV() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        String nowTime = simpleDateFormat.format(date);
        Map<String, Integer> map = new HashMap<>();
        Integer pv = Integer.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get("PV" + nowTime)));

        Integer ip = Math.toIntExact(redisTemplate.opsForHyperLogLog().size("IP" + nowTime));
        Integer uv = Math.toIntExact(redisTemplate.opsForHyperLogLog().size("UV" + nowTime));

        map.put("pv", pv);
        map.put("ip", ip);
        map.put("uv", uv);
        return map;
    }

    public Map<String, Integer> getYesterdayPVAndIPAndUV() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 将日期减去一天
        Date yesterday = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        String yesterdayDate = simpleDateFormat.format(yesterday);

        Visit yesterdayVisit = visitDao.getYesterdayVisit(yesterdayDate);
        Integer pv = yesterdayVisit.getPv();
        Integer ip = yesterdayVisit.getIp();
        Integer uv = yesterdayVisit.getUv();
        Map<String, Integer> map = new HashMap<>();
        map.put("pv", pv);
        map.put("ip", ip);
        map.put("uv", uv);
        return map;
    }




    public void addVisit(Visit visit) {
        visitDao.addVisit(visit);
    }
}
