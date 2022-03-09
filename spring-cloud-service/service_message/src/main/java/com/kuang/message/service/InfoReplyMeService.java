package com.kuang.message.service;

import com.kuang.message.entity.InfoReplyMe;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface InfoReplyMeService extends IService<InfoReplyMe> {

    //查找未读消息
    Integer findUserUnreadNumber(String userId);
}
