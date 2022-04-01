package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserHeadPortrait;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface UserHeadPortraitService extends IService<UserHeadPortrait> {

    //查询出所有头像
    List<String> findAllAvatar();
}
