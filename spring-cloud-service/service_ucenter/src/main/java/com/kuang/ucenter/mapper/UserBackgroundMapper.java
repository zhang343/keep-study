package com.kuang.ucenter.mapper;

import com.kuang.ucenter.entity.UserBackground;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


public interface UserBackgroundMapper extends BaseMapper<UserBackground> {

    //查看所有背景图像
    List<String> findAllBgimg();
}
