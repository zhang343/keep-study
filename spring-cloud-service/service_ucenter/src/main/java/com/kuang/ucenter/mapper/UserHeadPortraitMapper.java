package com.kuang.ucenter.mapper;

import com.kuang.ucenter.entity.UserHeadPortrait;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


public interface UserHeadPortraitMapper extends BaseMapper<UserHeadPortrait> {

    //查询出所有头像
    List<String> findAllAvatar();
}
