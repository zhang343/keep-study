package com.kuang.message.service;

import com.kuang.message.entity.InfoFriendFeed;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface InfoFriendFeedService extends IService<InfoFriendFeed> {

    //查询出所有好友动态,只查询id和文章id
    List<InfoFriendFeed> findAllFriendFeed();

    //查找未读消息
    Integer findUserUnreadNumber(String userId);
}
