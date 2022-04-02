package com.kuang.bbs.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserArticleVo {

    private String articleId;
    private String title;
    private String description;
    private String categoryName;
    private Long views;
    private Date gmtCreate;
    private Boolean isRelease;
    private Boolean isViolationArticle;
    private Boolean isExcellentArticle;
    private List<String> labelList;
}
