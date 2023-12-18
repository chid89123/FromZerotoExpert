package com.wang.fromzerotoexpert.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private PV_IP_UVInterceptor PVIpUVInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new UserLoginInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/login.html", "/index.ftl");

        registry.addInterceptor(PVIpUVInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login.html", "/register.html", "/login", "/register");
    }


}
