package com.kuang.bbs.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class TwoCommentVo {

    private String id;
    private String userId;
    private String userAvatar;
    private String userNickname;
    private String content;
    private String userVipLevel;
    private String replyUserId;
    private String replyUserNickname;
    private Date gmtCreate;
}
