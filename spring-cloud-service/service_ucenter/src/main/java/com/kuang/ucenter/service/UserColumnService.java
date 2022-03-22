package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserColumn;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserColumnService extends IService<UserColumn> {


    //查找用户专栏数量
    Integer findUserColumnNumber(String userId);
}
