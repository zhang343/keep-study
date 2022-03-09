package com.kuang.ucenter.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo {
    private String id;
    private String nickname;
    private String avatar;
    private String sign;
    private String vipLevel;
    private Date gmtCreate;
}
