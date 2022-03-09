package com.kuang.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.message.entity.InfoFriendFeed;
import com.kuang.message.mapper.InfoFriendFeedMapper;
import com.kuang.message.service.InfoFriendFeedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Service
@Slf4j
public class InfoFriendFeedServiceImpl extends ServiceImpl<InfoFriendFeedMapper, InfoFriendFeed> implements InfoFriendFeedService {

    //查询出所有好友动态,只查询id和文章id
    @Override
    public List<InfoFriendFeed> findAllFriendFeed() {
        log.info("查询出所有好友动态");
        QueryWrapper<InfoFriendFeed> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "article_id");
        return baseMapper.selectList(wrapper);
    }

    //查找未读消息
    @Override
    public Integer findUserUnreadNumber(String userId) {
        QueryWrapper<InfoFriendFeed> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("is_read" , 0);
        return baseMapper.selectCount(wrapper);
    }
}
