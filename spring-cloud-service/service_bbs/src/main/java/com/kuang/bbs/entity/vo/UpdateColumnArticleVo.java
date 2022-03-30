package com.kuang.bbs.entity.vo;


import lombok.Data;

@Data
public class UpdateColumnArticleVo {

    private String articleId;
    private String title;
    private String description;
    private String categoryId;
    private String content;
    private Boolean isBbs;
}
