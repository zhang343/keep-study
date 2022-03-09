package com.kuang.ucenter.entity.vo;

import lombok.Data;

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
    private Integer articleNumber;
    private Integer columnNumber;
    private Integer studyNumber;
    private Integer dynamicNumber;
}
