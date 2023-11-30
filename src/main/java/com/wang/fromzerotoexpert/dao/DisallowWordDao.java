package com.wang.fromzerotoexpert.dao;

import com.wang.fromzerotoexpert.domain.DisallowWord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DisallowWordDao {
    List<String> getAll();
}
