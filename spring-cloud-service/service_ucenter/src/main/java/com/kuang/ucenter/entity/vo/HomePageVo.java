package com.kuang.ucenter.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class HomePageVo {

    private String vipLevel;
    private Integer articleRealeaseNumber;
    private Integer commentNumber;
    private Integer studyNumber;
    private String account;
    private String nickname;
    private Integer experience;
    private Integer money;
    private Date gmtCreate;
}
