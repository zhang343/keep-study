package com.kuang.ucenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.ucenter.entity.UserHomepage;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserHomepageService extends IService<UserHomepage> {

    //查询用户简介
    String findUserIntroduce(String userId);

    //修改用户主页内容
    void setUserIntroduce(String userId, String content);
}
