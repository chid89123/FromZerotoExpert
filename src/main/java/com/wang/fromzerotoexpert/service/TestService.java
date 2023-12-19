package com.wang.fromzerotoexpert.service;

import com.wang.fromzerotoexpert.annotation.MyAnn;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestService {

    @MyAnn
    public Map<String, String> test() {
        Map<String, String> map = new HashMap<>();
        map.put("t1", "service设置的值");
        return map;
    }
}
