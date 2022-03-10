package com.kuang.message.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyMeVo {
    private String id;
    private String articleId;
    private String title;
    private String replyUserId;
    private String replyUserAvatar;
    private String replyUserNickname;
    private String vipLevel;
    private Date gmtCreate;
    private String content;
}
