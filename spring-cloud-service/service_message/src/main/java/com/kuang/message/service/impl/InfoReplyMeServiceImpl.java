package com.kuang.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.message.entity.InfoReplyMe;
import com.kuang.message.mapper.InfoReplyMeMapper;
import com.kuang.message.service.InfoReplyMeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Service
public class InfoReplyMeServiceImpl extends ServiceImpl<InfoReplyMeMapper, InfoReplyMe> implements InfoReplyMeService {

    //查找未读消息
    @Override
    public Integer findUserUnreadNumber(String userId) {
        QueryWrapper<InfoReplyMe> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("is_read" , 0);
        return baseMapper.selectCount(wrapper);
    }
}
