package com.kuang.ucenter.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserFollowOrFans {

    private String userId;
    private String avatar;
    private String nickname;
    private String vipLevel;
    private Date gmtCreate;
    private String sign;
}
