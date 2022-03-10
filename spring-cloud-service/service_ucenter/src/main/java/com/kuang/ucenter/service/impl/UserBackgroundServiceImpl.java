package com.kuang.ucenter.service.impl;

import com.kuang.ucenter.entity.UserBackground;
import com.kuang.ucenter.mapper.UserBackgroundMapper;
import com.kuang.ucenter.service.UserBackgroundService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-10
 */
@Service
public class UserBackgroundServiceImpl extends ServiceImpl<UserBackgroundMapper, UserBackground> implements UserBackgroundService {

    //查看所有背景图像
    @Cacheable(value = "bgImgList")
    @Override
    public List<String> findAll() {
        List<UserBackground> userBackgrounds = baseMapper.selectList(null);
        List<String> bgImgList = new ArrayList<>();
        for(UserBackground userBackground : userBackgrounds){
            bgImgList.add(userBackground.getUrl());
        }
        return bgImgList;
    }
}
