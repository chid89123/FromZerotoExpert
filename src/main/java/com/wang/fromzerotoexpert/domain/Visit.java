package com.wang.fromzerotoexpert.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Visit {
    private Long id;
    private Integer pv;
    private Integer ip;
    private Integer uv;
    private String date;
}
