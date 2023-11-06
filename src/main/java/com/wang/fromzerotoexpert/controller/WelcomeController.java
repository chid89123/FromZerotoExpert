package com.wang.fromzerotoexpert.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WelcomeController {

    @GetMapping("/index.ftl")
    public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("index");
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            System.out.println("初次来到");
            Cookie cookie = new Cookie("firstLogin", "1");
            cookie.setPath("/index.ftl");
            cookie.setMaxAge(60);
            response.addCookie(cookie);
            modelAndView.addObject("msg", "嗨，欢迎您来到 from zero to expert.");
        } else {
            System.out.println("再次来到");
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + "    " +cookie.getValue());
            }
            modelAndView.addObject("msg", "嗨，欢迎您再次来到 from zero to expert.");
        }

        return modelAndView;
    }
}
