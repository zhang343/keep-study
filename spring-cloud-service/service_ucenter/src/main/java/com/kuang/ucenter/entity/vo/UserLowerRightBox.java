package com.kuang.ucenter.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserLowerRightBox {

    private Integer articleRealeaseNumber;
    private Integer commentNumber;

    private String vipLevel;
    private String account;
    private Integer money;
    private Date gmtCreate;

    private Integer studyNumber;
}
