package com.kuang.course.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class IndexCategoryVo {

    //二级分类id
    private String id;
    //二级分类标题
    private String title;
    //二级分类下属课程
    private List<IndexCourseVo> courseList;
}
