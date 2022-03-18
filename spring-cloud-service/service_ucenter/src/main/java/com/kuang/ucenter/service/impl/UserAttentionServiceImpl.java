package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.client.VipClient;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
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


}
