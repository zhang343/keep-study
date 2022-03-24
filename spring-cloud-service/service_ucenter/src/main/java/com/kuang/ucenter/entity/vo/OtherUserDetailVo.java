package com.kuang.ucenter.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OtherUserDetailVo {

    //用户id
    private String id;
    //背景图像
    private String bgImg;
    //用户头像
    private String avatar;
    //用户昵称
    private String nickname;
    //用户性别
    private Boolean sex;
    //用户经验
    private Integer experience;
    //用户个性签名
    private String sign;


    //vip等级
    private String vipLevel;
    //用户关注数量
    private Integer attentionNumber;
    //用户粉丝
    private Integer fansNumber;
    //说说数量
    private Integer dynamicNumber;
    //已经发布到江湖还可以看到的文章
    private Integer bbsArticleNumber;
    //专栏数量
    private Integer columnNumber;
    //学习数量
    private Integer studyNumber;
    //所有的评论数量
    private Integer commentNumber;


    //账号
    private String account;
    //k币
    private Integer money;
    //注册时间
    private Date gmtCreate;
}
