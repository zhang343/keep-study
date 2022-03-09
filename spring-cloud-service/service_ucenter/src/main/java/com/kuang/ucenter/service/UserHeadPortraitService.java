package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserHeadPortrait;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xiaozhang
 * @since 2022-03-07
 */
public interface UserHeadPortraitService extends IService<UserHeadPortrait> {

    //查询出所有头像
    List<String> findAll();
}
