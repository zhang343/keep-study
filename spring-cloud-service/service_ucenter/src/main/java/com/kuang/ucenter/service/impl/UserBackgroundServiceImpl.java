package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.ucenter.entity.UserBackground;
import com.kuang.ucenter.mapper.UserBackgroundMapper;
import com.kuang.ucenter.service.UserBackgroundService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserBackgroundServiceImpl extends ServiceImpl<UserBackgroundMapper, UserBackground> implements UserBackgroundService {

    //查看所有背景图像
    @Cacheable(value = "bgImgList")
    @Override
    public List<String> findAllBgimg() {
        return baseMapper.findAllBgimg();
    }
}
