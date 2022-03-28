package com.kuang.bbs.entity.vo;

import lombok.Data;

@Data
public class PulishColumnArticleVo {

    private String title;
    private String description;
    private String categoryId;
    private String content;
    private Boolean isBbs;
}
