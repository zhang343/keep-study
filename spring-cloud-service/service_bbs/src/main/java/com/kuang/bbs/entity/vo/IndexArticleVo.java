package com.kuang.bbs.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class IndexArticleVo {
    //文章id
    private String id;
    //是不是置顶文章
    private Boolean isTop;
    //文章标题
    private String title;
    //文章描述
    private String description;
    //用户id
    private String userId;
    //用户头像
    private String avatar;
    //用户昵称
    private String nickname;
    //用户vip等级
    private String vipLevel;
    //文章浏览量
    private Long views;
    //文章分类名称
    private String categoryName;
    //是不是精品文章
    private Boolean isExcellentArticle;
    //文章发布时间
    private Date gmtCreate;
}
