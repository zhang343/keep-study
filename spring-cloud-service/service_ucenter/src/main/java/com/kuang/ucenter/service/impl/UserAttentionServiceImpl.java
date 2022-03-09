package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.UserAttention;
import com.kuang.ucenter.entity.UserInfo;
import com.kuang.ucenter.entity.vo.UserVo;
import com.kuang.ucenter.mapper.UserAttentionMapper;
import com.kuang.ucenter.mapper.UserInfoMapper;
import com.kuang.ucenter.service.UserAttentionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Service
@Slf4j
public class UserAttentionServiceImpl extends ServiceImpl<UserAttentionMapper, UserAttention> implements UserAttentionService {

    @Resource
    private UserInfoMapper userInfoMapper;

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
    @Async
    @Override
    public Future<Integer> findFocusOnNumber(String userId) {
        log.info("开始查询用户关注数量,用户id:" + userId);
        QueryWrapper<UserAttention> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        Integer integer = baseMapper.selectCount(wrapper);
        return new AsyncResult<>(integer);
    }

    //查询用户关注用户
    @Async
    @Override
    public Future<List<UserVo>> findFocusOnUser(String userId, Long current, Long limit) {
        log.info("开始查询用户关注,用户id:" + userId);
        current = (current - 1) * limit;
        List<UserVo> userVoList = baseMapper.findFocusOnUser(userId , current , limit);
        return new AsyncResult<>(userVoList);
    }

    //查询用户粉丝数量
    @Async
    @Override
    public Future<Integer> findFansNumber(String userId) {
        log.info("开始查询用户粉丝数量,用户id:" + userId);
        QueryWrapper<UserAttention> wrapper = new QueryWrapper<>();
        wrapper.eq("attention_user_id" , userId);
        Integer integer = baseMapper.selectCount(wrapper);
        return new AsyncResult<>(integer);
    }

    //查询用户粉丝
    @Async
    @Override
    public Future<List<UserVo>> findFansUser(String userId, Long current, Long limit) {
        log.info("开始查询用户粉丝,用户id:" + userId);
        current = (current - 1) * limit;
        List<UserVo> userVoList = baseMapper.findFansUser(userId , current , limit);
        return new AsyncResult<>(userVoList);
    }

    //增加用户关注
    @Override
    public void addUserAttention(String myUserId, String userId) {
        log.info("用户" + myUserId + "去关注用户:" + userId);
        boolean flag = findUserAtoUserB(myUserId, userId);
        if(flag){
            throw new XiaoXiaException(ResultCode.ERROR , "你已经关注了");
        }

        UserInfo userInfo = userInfoMapper.selectById(userId);
        if(userInfo == null){
            throw new XiaoXiaException(ResultCode.ERROR , "用户不存在");
        }

        UserAttention userAttention = new UserAttention();
        userAttention.setUserId(myUserId);
        userAttention.setAttentionUserId(userId);
        userAttention.setAvatar(userInfo.getAvatar());
        userAttention.setNickname(userInfo.getNickname());
        userAttention.setSign(userInfo.getSign());

        int insert = baseMapper.insert(userAttention);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "关注失败");
        }
    }

    //查询用户A是否关注了用户B
    @Override
    public boolean findUserAtoUserB(String userIdA, String userIdB) {
        log.info("查询用户A是否关注了用户B,用户A:" + userIdA + ",用户B:" + userIdB);
        QueryWrapper<UserAttention> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userIdA);
        wrapper.eq("attention_user_id" , userIdB);
        return baseMapper.selectCount(wrapper) != 0;
    }

    //查询用户粉丝数量
    @Override
    public Integer findUserFansNumber(String userId) {
        log.info("查询用户粉丝数量");
        QueryWrapper<UserAttention> wrapper = new QueryWrapper<>();
        wrapper.eq("attention_user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查询用户关注数量
    @Override
    public Integer findUserAttentionNumber(String userId) {
        log.info("查询用户关注数量");
        QueryWrapper<UserAttention> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }
}
