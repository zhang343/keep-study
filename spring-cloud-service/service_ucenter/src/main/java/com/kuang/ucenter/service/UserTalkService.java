package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserTalk;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserTalkService extends IService<UserTalk> {

    //查询出用户说说数量
    Integer findDynamicNumberByUserId(String userId);
}
