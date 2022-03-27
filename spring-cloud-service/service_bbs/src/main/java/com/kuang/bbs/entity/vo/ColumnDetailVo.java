package com.kuang.bbs.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class ColumnDetailVo {

    //专栏id
    private String columnId;
    //专栏名称
    private String title;
    //专栏描述
    private String description;
    //可见度
    private Long vsibility;
    //浏览数
    private Long views;
    //渐变色
    private String color;
    //是否发布
    private Boolean isRelease;
    //作者
    private List<ColumnAuthorVo> authorList;

}
