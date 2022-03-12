package com.kuang.bbs.entity.vo;

import lombok.Data;

@Data
public class ArticleUpdateAndCreateVo{
    private String id;
    private String title;
    private String description;
    private String content;
    private String categoryId;
    private Boolean isRelease;
}
