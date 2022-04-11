package com.kuang.bbs.entity.admin;

import lombok.Data;

import java.util.Date;

@Data
public class AdminReportArticleVo {

    private String id;
    private String articleId;
    private String title;
    //举报内容
    private String content;
    private Date gmtCreate;
}
