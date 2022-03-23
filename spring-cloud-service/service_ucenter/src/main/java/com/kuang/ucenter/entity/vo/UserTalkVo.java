package com.kuang.ucenter.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserTalkVo {
    private String id;
    private Boolean isPublic;
    private String content;
    private Date gmtCreate;
}
