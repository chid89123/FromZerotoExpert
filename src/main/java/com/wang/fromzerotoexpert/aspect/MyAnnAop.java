package com.wang.fromzerotoexpert.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class MyAnnAop {

    //标记annotation下的Myann注解，是切点
    @Pointcut("@annotation(com.wang.fromzerotoexpert.annotation.MyAnn)")
    public void ann(){
    }


    @Before("ann()")
    public void before(JoinPoint joinPoint){
        System.out.println("打印：开始前");
    }


    @AfterReturning(value = "ann()",returning = "res")
    public Object dochange(JoinPoint joinPoint, Object res){
        System.out.println("AfterReturning通知开始-获取数据:" + res);
        //获取数据
        Map<String,String> map= (Map<String, String>) res;
        //添加新值
        map.put("s1","我是在AOP中添加的新值");
        return map;
    }
}
