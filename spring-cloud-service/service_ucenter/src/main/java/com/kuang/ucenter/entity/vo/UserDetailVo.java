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


    //vip等级
    private String vipLevel;
    private Integer attentionNumber;
    private Integer fansNumber;
    //已经发布到江湖还可以看到的文章
    private Integer bbsArticleNumber;
    private Integer studyNumber;
    //所有的评论数量
    private Integer commentNumber;




    private Integer allArticleNumber;
    private Integer columnNumber;
    private Integer dynamicNumber;





    //账号
    private String account;
    //k币
    private Integer money;
    //注册时间
    private Date gmtCreate;
}
