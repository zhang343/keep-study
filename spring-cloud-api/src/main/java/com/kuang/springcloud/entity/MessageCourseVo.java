package com.kuang.springcloud.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MessageCourseVo {

    private String id;
    private String cover;
    private String title;
    private String description;
    private String teacher;
    private Integer views;
    private Integer videoNumber;
    private Date gmtModified;
}
