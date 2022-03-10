package com.kuang.springcloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoFriendFeedVo implements Serializable {

    private String articleId;
    private String title;
    private String description;
    private String attationUserId;
    private String attationUserNickname;
    private String attationUserAvatar;
    private String categoryName;
    private List<String> userIdList;
}
