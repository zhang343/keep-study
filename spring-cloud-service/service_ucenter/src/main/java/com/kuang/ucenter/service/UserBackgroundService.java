package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserBackground;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface UserBackgroundService extends IService<UserBackground> {

    //查看所有背景图像
    List<String> findAllBgimg();
}
