package com.wang.fromzerotoexpert;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/**");
    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 让Spring Boot可以访问静态资源，例如HTML文件
//        registry.addResourceHandler("/templates/**")
//                .addResourceLocations("classpath:/templates/");
//    }

}
