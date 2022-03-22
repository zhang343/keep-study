package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.ucenter.entity.UserTalk;
import com.kuang.ucenter.mapper.UserTalkMapper;
import com.kuang.ucenter.service.UserTalkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Service
@Slf4j
public class UserTalkServiceImpl extends ServiceImpl<UserTalkMapper, UserTalk> implements UserTalkService {

    //查询用户说说数量
    @Override
    public Integer findUserTalkNumber(String userId) {
        QueryWrapper<UserTalk> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }
}
