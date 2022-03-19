package com.kuang.ucenter.entity.vo;

import lombok.Data;

@Data
public class MyUserInfoVo {
    private String id;
    private String avatar;
    private String nickname;
    private Integer bbsArticleNumber;
    private Integer attentionNumber;
    private Integer fansNumber;
    private Boolean isSignIn;
    private String vipLevel;
    private String account;
    private Integer experience;
    private Integer money;
}
