package com.wang.fromzerotoexpert.controller;

import com.wang.fromzerotoexpert.handler.ConditionException;
import com.wang.fromzerotoexpert.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserSupport userSupport;

//    @GetMapping("/index.ftl")
//    public String index(Model model) {
//        model.addAttribute("msg", "nihao");
//        return "index";
//    }

    @GetMapping("test")
    public String test() throws ConditionException {
        Long userId = userSupport.getCurrentUserId();
        return "nihao";
    }
}
