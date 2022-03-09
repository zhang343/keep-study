package com.kuang.bbs.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleVo {
    //文章id
    private String id;
    //文章标题
    private String title;
    //是不是精品文章
    private Boolean isExcellentArticle;
    //是不是草稿
    private Boolean isRelease;
    //用户id
    private String userId;
    //用户头像
    private String avatar;
    //用户昵称
    private String nickname;
    //用户vip等级
    private String vipLevel;
    //文章分类名称
    private String categoryName;
    //文章浏览量
    private Long views;
    //文章内容
    private String content;

    private Date gmtCreate;

    private Date gmtModified;
}
