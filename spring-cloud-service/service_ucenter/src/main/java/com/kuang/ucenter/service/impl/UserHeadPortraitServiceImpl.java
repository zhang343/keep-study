package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.ucenter.entity.UserHeadPortrait;
import com.kuang.ucenter.mapper.UserHeadPortraitMapper;
import com.kuang.ucenter.service.UserHeadPortraitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class UserHeadPortraitServiceImpl extends ServiceImpl<UserHeadPortraitMapper, UserHeadPortrait> implements UserHeadPortraitService {

    //查询出所有头像
    @Cacheable(value = "avatarList")
    @Override
    public List<String> findAllAvatar() {
        return baseMapper.findAllAvatar();
    }
}
