package com.kuang.bbs.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OneCommentVo {

    private String id;
    private String userId;
    private String userAvatar;
    private String userNickname;
    private String content;
    private String vipLevel;
    private Date gmtCreate;
    private List<TwoCommentVo> childList;
}
