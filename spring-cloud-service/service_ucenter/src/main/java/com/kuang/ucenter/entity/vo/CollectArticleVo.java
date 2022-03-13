package com.kuang.ucenter.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CollectArticleVo {

    private String id;
    private String title;
    private String description;
    private String categoryName;
    private Integer views;
    private Boolean isExcellentArticle;
    private Date gmtCreat;
}
