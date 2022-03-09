package com.kuang.springcloud.entity;

import lombok.Data;

@Data
public class InfoMyNewsVo {

    private String userId;
    private Boolean isCourse;
    private String courseTitle;
    private String courseId;
}
