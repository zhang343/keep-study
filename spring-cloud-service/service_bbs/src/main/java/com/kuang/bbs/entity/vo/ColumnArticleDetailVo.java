package com.kuang.bbs.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ColumnArticleDetailVo {

    //文章id
    private String articleId;
    //文章标题
    private String title;
    //用户id
    private String userId;
    //用户头像
    private String avatar;
    //用户昵称
    private String nickname;
    //文章分类名称
    private String categoryName;
    //文章内容
    private String content;

    private Date gmtCreate;

    private Date gmtModified;
}
