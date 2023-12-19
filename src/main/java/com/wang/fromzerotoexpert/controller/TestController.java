package com.wang.fromzerotoexpert.controller;

import com.wang.fromzerotoexpert.handler.ConditionException;
import com.wang.fromzerotoexpert.service.TestService;
import com.wang.fromzerotoexpert.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;

    @Autowired
    private UserSupport userSupport;

//    @GetMapping("/index.ftl")
//    public String index(Model model) {
//        model.addAttribute("msg", "nihao");
//        return "index";
//    }

    @GetMapping("/test1")
    public String test() throws ConditionException {
        Long userId = userSupport.getCurrentUserId();
        return "nihao";
    }

    @GetMapping("/test2")
    public String test2() {
        Map<String, String > map = testService.test();
        Set<String> set = map.keySet();
        for (String s : set) {
            System.out.println(s + " " + map.get(s));
        }
        return "success";
    }


}
