package com.kuang.message.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MyNewsVo {

    private String id;
    private String title;
    private String content;
    private Boolean isCourse;
    private String courseId;
    private Date gmtCreate;
}
