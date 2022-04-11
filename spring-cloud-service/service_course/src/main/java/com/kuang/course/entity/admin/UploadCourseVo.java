package com.kuang.course.entity.admin;

import lombok.Data;

@Data
public class UploadCourseVo {

    private String ocId;
    private String tcId;
    private Integer sort;
    private String title;
    private String description;
    private String cover;
    private String status;
    private Integer price;
}
