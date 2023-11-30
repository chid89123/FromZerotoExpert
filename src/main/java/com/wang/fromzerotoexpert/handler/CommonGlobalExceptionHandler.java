package com.wang.fromzerotoexpert.handler;

import com.wang.fromzerotoexpert.domain.JsonResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice //该注解表明这是一个全局异常处理类，用于捕获在控制器中抛出的异常。
@Order(Ordered.HIGHEST_PRECEDENCE) //通过设置优先级为 HIGHEST_PRECEDENCE，确保这个异常处理器的优先级最高，这样它将首先被调用。
public class CommonGlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e) {
        String errormsg = e.getMessage();
        if (e instanceof ConditionException) {
            String errorCode = ((ConditionException) e).getCode();
            return new JsonResponse<>(errorCode, errormsg);
        } else {
            return new JsonResponse<>("500", errormsg);
        }
    }
}
