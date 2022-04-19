package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import com.kuang.ucenter.entity.UserAttention;
import com.kuang.ucenter.entity.vo.UserFollowOrFans;
import com.kuang.ucenter.mapper.UserAttentionMapper;
import com.kuang.ucenter.service.UserAttentionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class UserAttentionServiceImpl extends ServiceImpl<UserAttentionMapper, UserAttention> implements UserAttentionService {

    //查询出指定用户的所有粉丝id
    @Override
    public List<String> findUserFansId(String userId) {
        log.info("查询出指定用户的所有粉丝id,用户id:" + userId);
        QueryWrapper<UserAttention> wrapper = new QueryWrapper<>();
        wrapper.select("user_id");
        wrapper.eq("attention_user_id" , userId);
        List<UserAttention> userAttentionList = baseMapper.selectList(wrapper);
        List<String> userIdList = new ArrayList<>();
        for(UserAttention userAttention : userAttentionList){
            userIdList.add(userAttention.getUserId());
        }
        return userIdList;
    }

    //查询用户关注数量
    @Override
    public Integer findUserFollowNumber(String userId) {
        QueryWrapper<UserAttention> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查询用户粉丝数量
    @Override
    public Integer findUserFansNumber(String userId) {
        QueryWrapper<UserAttention> wrapper = new QueryWrapper<>();
        wrapper.eq("attention_user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查询关注用户
    @Override
    public List<UserFollowOrFans> findUserFollow(String userId, Long current, Long limit) {
        current = (current - 1) * limit;
        List<UserFollowOrFans> userFollowOrFansList =  baseMapper.findUserFollow(userId , current , limit);
        if(userFollowOrFansList == null || userFollowOrFansList.size() == 0){
            return userFollowOrFansList;
        }
        VipUtils.setVipLevel(userFollowOrFansList , userFollowOrFansList.get(0));
        return userFollowOrFansList;
    }

    //查询粉丝
    @Override
    public List<UserFollowOrFans> findUserFans(String userId, Long current, Long limit) {
        current = (current - 1) * limit;
        List<UserFollowOrFans> userFollowOrFansList =  baseMapper.findUserFans(userId , current , limit);
        if(userFollowOrFansList == null || userFollowOrFansList.size() == 0){
            return userFollowOrFansList;
        }
        VipUtils.setVipLevel(userFollowOrFansList , userFollowOrFansList.get(0));
        return userFollowOrFansList;
    }

    //查询A是否关注了B
    @Override
    public boolean findAIsAttentionB(String myuserId, String userId) {
        QueryWrapper<UserAttention> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , myuserId);
        wrapper.eq("attention_user_id" , userId);
        return baseMapper.selectCount(wrapper) != 0;
    }

    //增加用户关注
    @Override
    public void addUserAttention(String userId, String otherUserId) {
        boolean aIsAttentionB = findAIsAttentionB(userId, otherUserId);
        if(aIsAttentionB){
            throw new XiaoXiaException(ResultCode.ERROR , "你已经关注了，请不要重复操作");
        }
        UserAttention userAttention = new UserAttention();
        userAttention.setUserId(userId);
        userAttention.setAttentionUserId(otherUserId);
        int insert = baseMapper.insert(userAttention);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "关注失败");
        }
    }

    //取消用户关注
    @Override
    public void deleteUserAttention(String userId, String otherUserId) {
        QueryWrapper<UserAttention> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("attention_user_id" , otherUserId);
        int delete = baseMapper.delete(wrapper);
        if(delete != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "取消关注失败");
        }
    }


}
