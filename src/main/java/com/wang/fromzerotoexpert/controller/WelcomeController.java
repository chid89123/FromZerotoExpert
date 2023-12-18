package com.wang.fromzerotoexpert.controller;

import com.wang.fromzerotoexpert.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class WelcomeController {
    @Autowired
    private WelcomeService welcomeService;

    @GetMapping("/index")
    public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("index");
        Cookie[] cookies = request.getCookies();

        Cookie cookie = new Cookie("firstLogin", "1");
        cookie.setPath("/index");
        cookie.setMaxAge(60 * 60 * 24);
        boolean flag = false;

        if (cookies == null) {
//            System.out.println("初次来到");
            response.addCookie(cookie);
            modelAndView.addObject("msg", "嗨，欢迎您来到 from zero to expert.");
        } else {
//            System.out.println("再次来到");
            for (Cookie cookie1 : cookies) {
                if (cookie1.getName().equals("firstLogin")) {
                    modelAndView.addObject("msg", "嗨，欢迎您再次来到 from zero to expert.");
                    flag = true;
                }
            }
            if (!flag) {
                response.addCookie(cookie);
                modelAndView.addObject("msg", "嗨，欢迎您来到 from zero to expert.");
            }
        }


        Map<String, Integer> todayMap = welcomeService.getTodayPVAndIPAndUV();
        Map<String, Integer> yesterdayMap = welcomeService.getYesterdayPVAndIPAndUV();

        modelAndView.addObject("pv", todayMap.get("pv"));
        modelAndView.addObject("ip", todayMap.get("ip"));
        modelAndView.addObject("uv", todayMap.get("uv"));

        modelAndView.addObject("yesterdayPv", yesterdayMap.get("pv"));
        modelAndView.addObject("yesterdayIp", yesterdayMap.get("ip"));
        modelAndView.addObject("yesterdayUv", yesterdayMap.get("uv"));

        return modelAndView;
    }

}
