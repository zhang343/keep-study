package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserBackground;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-10
 */
public interface UserBackgroundService extends IService<UserBackground> {

    //查看所有背景图像
    List<String> findAll();
}
