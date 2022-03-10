package com.kuang.message.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FriendFeedVo {
    private String id;
    private String articleId;
    private String title;
    private String description;
    private Integer views;
    private Date gmtCreate;
    private String attationUserId;
    private String attationUserAvatar;
    private String attationUserNickname;
    private String vipLevel;
    private String categoryName;
}
