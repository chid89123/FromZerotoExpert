package com.wang.fromzerotoexpert.dao;

import com.wang.fromzerotoexpert.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    Integer addUser(User user);

    User getUserByUsername(String username);
}
