package com.wang.fromzerotoexpert.dao;

import com.wang.fromzerotoexpert.domain.Visit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VisitDao {


    void addVisit(Visit visit);

    Visit getYesterdayVisit(String yesterdayDate);
}
