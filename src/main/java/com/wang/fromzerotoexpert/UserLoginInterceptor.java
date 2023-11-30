package com.wang.fromzerotoexpert;

import com.wang.fromzerotoexpert.domain.User;
import com.wang.fromzerotoexpert.handler.ConditionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    /**
     * true 表示继续流程 false表示中断
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        User user = (User) request.getSession().getAttribute("currentUser");
        if (user == null) {
            log.info("user=null");
            throw new ConditionException("用户未登录");
            //return ResponseVo.error(ResponseEnum.NEED_LOGIN);
            //response.getWriter().print("error");
            //return false;
        }
        //return HandlerInterceptor.super.preHandle(request, response, handler);
        return true;
    }
}
