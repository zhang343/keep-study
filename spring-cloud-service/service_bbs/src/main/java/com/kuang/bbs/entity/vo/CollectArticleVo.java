package com.kuang.bbs.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CollectArticleVo {

    private String id;
    private String articleId;
    private String title;
    private String description;
    private String categoryName;
    private Long views;
    private Date gmtCreate;
    private Boolean isExcellentArticle;
}
