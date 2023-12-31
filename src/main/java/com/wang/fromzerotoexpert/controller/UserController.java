package com.wang.fromzerotoexpert.controller;

import com.wang.fromzerotoexpert.form.UserLoginForm;
import com.wang.fromzerotoexpert.handler.ConditionException;
import com.wang.fromzerotoexpert.domain.JsonResponse;
import com.wang.fromzerotoexpert.domain.User;
import com.wang.fromzerotoexpert.service.UserService;
import com.wang.fromzerotoexpert.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserSupport userSupport;

    @PostMapping("/userRegister")
    public JsonResponse<String> userRegister(User user) throws ConditionException {
        userService.addUser(user);
        return JsonResponse.success();
    }

    @PostMapping("/userRegisterJson")
    public JsonResponse<String> userRegisterJson(@RequestBody User user) throws ConditionException {
        userService.addUser(user);
        return JsonResponse.success();
    }


    @PostMapping("/login")
    public JsonResponse<String> login(UserLoginForm userLoginForm) throws Exception {
        String token = userService.login(userLoginForm);
        return new JsonResponse<>(token);
    }

    @PostMapping("/loginOnlyOne")
    public JsonResponse<User> login2(UserLoginForm userLoginForm,
                                     HttpServletRequest request) throws Exception {
        User user = userService.loginOnlyOne(userLoginForm, request);
        return new JsonResponse<>(user);
    }

    @PostMapping("/loginLimit")
    public JsonResponse<String> loginLimit(UserLoginForm userLoginForm,
                                         HttpServletRequest request) throws Exception {
        String token = userService.loginLimit(userLoginForm, request);
        return new JsonResponse<>(token);
    }

        @GetMapping("/getOnlineUser")
    public JsonResponse<String> getOnlineUser() {
        Integer onlineUser = userService.getOnlineUser();
        return new JsonResponse<>(String.valueOf(onlineUser));
    }

    @PostMapping("/OnlineUser")
    public JsonResponse<String> onlineUser() throws ConditionException {
        Long userId = userSupport.getCurrentUserId();
        userService.onlineUser(userId);
        return JsonResponse.success();
    }
}

