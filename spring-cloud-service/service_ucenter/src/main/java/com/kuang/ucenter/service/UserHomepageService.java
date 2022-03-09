package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserHomepage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.ucenter.entity.vo.HomePageVo;

import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserHomepageService extends IService<UserHomepage> {

    //修改用户主页内容
    void updateContent(String content , String userId);
}
