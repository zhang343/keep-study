package com.kuang.springcloud.entity;

import lombok.Data;

@Data
public class InfoReplyMeVo {
    private String userId;
    private String articleId;
    private String title;
    private String replyUserId;
    private String replyUserNickname;
    private String replyUserAvatar;
    private String content;
}
