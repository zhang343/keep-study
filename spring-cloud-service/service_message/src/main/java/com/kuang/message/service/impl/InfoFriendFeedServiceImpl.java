package com.kuang.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.message.client.BbsClient;
import com.kuang.message.entity.InfoFriendFeed;
import com.kuang.message.entity.vo.FriendFeedVo;
import com.kuang.message.mapper.InfoFriendFeedMapper;
import com.kuang.message.service.InfoFriendFeedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.VipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


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
        //查出数据
        current = (current - 1) * limit;
        List<FriendFeedVo> userNews = baseMapper.findUserNews(current, limit, userId);
        if(userNews == null || userNews.size() == 0){
            return userNews;
        }

        //设置vip标识
        VipUtils.setVipLevel(userNews , userNews.get(0) , "setVipLevel" , "getAttationUserId");


        //获取文章idList
        List<String> articleIdList = new ArrayList<>();
        for(FriendFeedVo friendFeedVo : userNews){
            articleIdList.add(friendFeedVo.getArticleId());
        }
        //远程调用获取文章浏览量和设置文章浏览量
        R articleViewsR = bbsClient.findArticleViews(articleIdList);
        if(articleViewsR.getSuccess()){
            Map<String , Object> views = articleViewsR.getData();
            for(FriendFeedVo friendFeedVo : userNews){
                Object o = views.get(friendFeedVo.getArticleId());
                if(o != null){
                    friendFeedVo.setViews((Integer) o);
                }
            }
        }
        return userNews;
    }

    //设置好友动态消息已读
    @Async
    @Override
    public void setFriendFeedRead(List<FriendFeedVo> friendFeedVos) {
        List<String> idList = new ArrayList<>();
        for(FriendFeedVo friendFeedVo : friendFeedVos){
            idList.add(friendFeedVo.getId());
        }
        if(idList.size() != 0){
            baseMapper.setFriendFeedRead(idList);
        }
    }
}
