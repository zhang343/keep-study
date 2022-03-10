package com.kuang.message.service;

import com.kuang.message.entity.InfoFriendFeed;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.message.entity.vo.FriendFeedVo;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface InfoFriendFeedService extends IService<InfoFriendFeed> {

    //查询出所有好友动态,只查询id和文章id
    List<InfoFriendFeed> findAllFriendFeed();

    //查找未读消息
    Integer findUserUnreadNumber(String userId);

    //查询好友动态消息数量
    Integer findUserNewsNumber(String userId);

    //查询好友动态消息
    List<FriendFeedVo> findUserNews(Long current, Long limit, String userId);

    //设置好友动态消息已读
    void setFriendFeedRead(List<FriendFeedVo> friendFeedVos);

    //查找文章浏览量和vip
    Future<List<FriendFeedVo>> findArticleViewsAndVipLevel(List<FriendFeedVo> friendFeedVos);
}
