package com.kuang.course.entity.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

@Data
public class CourseVo {

    //课程id
    private String id;
    //课程标题
    private String title;
    //课程描述
    private String description;
    //课程价格
    private Integer price;
    //课程时长，这里是
    private String totalLength;
    //课程小节数
    private Integer videoNumber;
    //课程播放量
    private Long views;
    //课程概述
    private String overview;
    //讲师介绍
    private String teacher;
    //适合人群
    private String suitablePeople;
    //课程安排
    private String courseArrange;
    //课程反馈
    private String courseFeedback;


    public void setTotalLength(Integer totalLength) {
        this.totalLength = DateUtil.secondToTime(totalLength);
    }
}
