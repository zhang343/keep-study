package com.kuang.course.entity.admin;

import lombok.Data;

@Data
public class UploadCourseDetailsVo {

    private String courseId;
    private String overview;
    private String teacher;
    private String suitablePeople;
    private String courseArrange;
    private String courseFeedback;
}
