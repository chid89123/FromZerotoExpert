package com.wang.fromzerotoexpert.controller;

import com.wang.fromzerotoexpert.domain.ConditionException;
import com.wang.fromzerotoexpert.domain.JsonResponse;
import com.wang.fromzerotoexpert.domain.User;
import com.wang.fromzerotoexpert.form.UserRegisterForm;
import com.wang.fromzerotoexpert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
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
}
