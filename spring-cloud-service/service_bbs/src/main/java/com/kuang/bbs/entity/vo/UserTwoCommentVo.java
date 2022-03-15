package com.kuang.bbs.entity.vo;

import lombok.Data;

@Data
public class UserTwoCommentVo {

    private String articleId;
    private String userId;
    private String userAvatar;
    private String userNickname;
    private String content;
    private String fatherId;
    private String replyUserId;
    private String replyUserNickname;
}
