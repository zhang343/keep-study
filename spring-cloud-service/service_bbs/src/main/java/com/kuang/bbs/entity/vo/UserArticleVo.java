package com.kuang.bbs.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserArticleVo {
    private String id;
    private String title;
    private String description;
    private String categoryName;
    private Integer views;
    private Date gmtCreate;
    private Boolean isRelease;
    private Boolean isViolationArticle;
    private Boolean isExcellentArticle;
}
