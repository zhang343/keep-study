package com.kuang.bbs.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OtherUserArticleVo {

    private String articleId;
    private String title;
    private String description;
    private String categoryName;
    private Long views;
    private Date gmtCreate;
    private Boolean isExcellentArticle;
    private List<String> labelList;
}
