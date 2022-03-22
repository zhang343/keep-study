package com.kuang.ucenter.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserDetailVo {
    private String id;
    private String bgImg;
    private String avatar;
    private String nickname;
    private Boolean sex;
    private Integer experience;
    private String sign;
    private Integer attentionNumber;
    private Integer fansNumber;
    private Integer allArticleNumber;
    private Integer columnNumber;
    private Integer studyNumber;
    private Integer dynamicNumber;
    //vip等级
    private String vipLevel;
    //已经发布到江湖还可以看到的文章
    private Integer bbsArticleNumber;
    //所有的评论数量
    private Integer commentNumber;
    //账号
    private String account;
    //k币
    private Integer money;
    //注册时间
    private Date gmtCreate;
}
