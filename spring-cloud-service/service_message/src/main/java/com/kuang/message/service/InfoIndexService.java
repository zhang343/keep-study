package com.kuang.message.service;

import com.kuang.message.entity.vo.MessageVo;

import java.util.concurrent.Future;

public interface InfoIndexService {

    //查询我的消息、好友动态、回复我的
    Future<MessageVo> findMFR(MessageVo messageVo, String userId);
}
