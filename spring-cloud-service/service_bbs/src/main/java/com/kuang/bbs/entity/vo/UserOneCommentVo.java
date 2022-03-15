package com.kuang.bbs.entity.vo;

import lombok.Data;

@Data
public class UserOneCommentVo {

    private String articleId;
    private String userId;
    private String userAvatar;
    private String userNickname;
    private String content;

}
