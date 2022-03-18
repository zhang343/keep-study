package com.kuang.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.message.client.BbsClient;
import com.kuang.message.client.VipClient;
import com.kuang.message.entity.InfoFriendFeed;
import com.kuang.message.entity.vo.FriendFeedVo;
import com.kuang.message.mapper.InfoFriendFeedMapper;
import com.kuang.message.service.InfoFriendFeedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.VipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Service
@Slf4j
public class InfoFriendFeedServiceImpl extends ServiceImpl<InfoFriendFeedMapper, InfoFriendFeed> implements InfoFriendFeedService {

    @Resource
    private BbsClient bbsClient;


    //查找未读消息
    @Override
    public Integer findUserUnreadNumber(String userId) {
        QueryWrapper<InfoFriendFeed> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("is_read" , 0);
        return baseMapper.selectCount(wrapper);
    }

    //查询好友动态消息数量
    @Override
    public Integer findUserNewsNumber(String userId) {
        QueryWrapper<InfoFriendFeed> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查询好友动态消息
    @Override
    public List<FriendFeedVo> findUserNews(Long current, Long limit, String userId) {
        current = (current - 1) * limit;
        List<FriendFeedVo> userNews = baseMapper.findUserNews(current, limit, userId);
        if(userNews == null || userNews.size() == 0){
            return userNews;
        }

        List<String> userIdList = new ArrayList<>();
        List<String> articleIdList = new ArrayList<>();
        for(FriendFeedVo friendFeedVo : userNews){
            userIdList.add(friendFeedVo.getAttationUserId());
            articleIdList.add(friendFeedVo.getArticleId());
        }

        Map<String , String> userVipLevel = VipUtils.getUserVipLevel(userIdList);
        if(userVipLevel == null){
            userVipLevel = new HashMap<>();
        }
        R articleViewsR = bbsClient.findArticleViews(articleIdList);
        Map<String , Object> views = articleViewsR.getData();
        for(FriendFeedVo friendFeedVo : userNews){
            friendFeedVo.setVipLevel(userVipLevel.get(friendFeedVo.getAttationUserId()));
            Object o = views.get(friendFeedVo.getArticleId());
            if(o != null){
                friendFeedVo.setViews((Integer) o);
            }
        }

        return userNews;
    }

    //设置好友动态消息已读
    @Async
    @Override
    public void setFriendFeedRead(List<FriendFeedVo> friendFeedVos) {
        log.info("好友动态消息已读");
        List<String> idList = new ArrayList<>();
        for(FriendFeedVo friendFeedVo : friendFeedVos){
            idList.add(friendFeedVo.getId());
        }
        if(idList.size() != 0){
            baseMapper.setFriendFeedRead(idList);
        }
    }
}
