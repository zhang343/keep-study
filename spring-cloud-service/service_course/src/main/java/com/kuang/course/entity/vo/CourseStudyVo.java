package com.kuang.course.entity.vo;

import lombok.Data;

@Data
public class CourseStudyVo {

    //课程id
    private String courseId;
    //课程标题
    private String title;
    //课程封面
    private String cover;
    //课程播放量
    private Long views;
}
