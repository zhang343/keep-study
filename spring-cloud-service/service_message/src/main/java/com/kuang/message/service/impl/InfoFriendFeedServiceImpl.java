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
    private VipClient vipClient;

    @Resource
    private BbsClient bbsClient;


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
        return baseMapper.findUserNews(current, limit, userId);
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

    //查找文章浏览量和vip
    @Async
    @Override
    public Future<List<FriendFeedVo>> findArticleViewsAndVipLevel(List<FriendFeedVo> friendFeedVos) {

        if(friendFeedVos == null || friendFeedVos.size() == 0){
            return new AsyncResult<>(friendFeedVos);
        }
        //获取文章id和用户id
        Set<String> userIdSet = new HashSet<>();
        Set<String> articleIdSet = new HashSet<>();
        for(FriendFeedVo friendFeedVo : friendFeedVos){
            userIdSet.add(friendFeedVo.getAttationUserId());
            articleIdSet.add(friendFeedVo.getArticleId());
        }


        List<String> userIdList = new ArrayList<>(userIdSet);
        List<String> articleIdList = new ArrayList<>(articleIdSet);
        //获取用户vip
        R memberRightLogo = vipClient.findMemberRightLogo(userIdList);
        Map<String , Object> mapVip = memberRightLogo.getData();
        //获取文章浏览量
        R articleViews = bbsClient.findArticleViews(articleIdList);
        Map<String , Object> mapViews = articleViews.getData();
        //设值
        for(FriendFeedVo friendFeedVo : friendFeedVos){
            friendFeedVo.setVipLevel((String) mapVip.get(friendFeedVo.getAttationUserId()));
            friendFeedVo.setViews((Integer) mapViews.get(friendFeedVo.getArticleId()));
        }
        return new AsyncResult<>(friendFeedVos);
    }
}
