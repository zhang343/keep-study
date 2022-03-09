package com.kuang.ucenter.service.impl;

import com.kuang.ucenter.entity.UserHeadPortrait;
import com.kuang.ucenter.mapper.UserHeadPortraitMapper;
import com.kuang.ucenter.service.UserHeadPortraitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xiaozhang
 * @since 2022-03-07
 */
@Service
@Slf4j
public class UserHeadPortraitServiceImpl extends ServiceImpl<UserHeadPortraitMapper, UserHeadPortrait> implements UserHeadPortraitService {

    //查询出所有头像
    @Cacheable(value = "avatarList")
    @Override
    public List<String> findAll() {
        log.info("查询出所有头像");
        List<UserHeadPortrait> userHeadPortraits = baseMapper.selectList(null);
        List<String> urlList = new ArrayList<>();
        for(UserHeadPortrait userHeadPortrait : userHeadPortraits){
            urlList.add(userHeadPortrait.getUrl());
        }
        return urlList;
    }
}
