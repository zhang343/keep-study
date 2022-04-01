package com.kuang.message.service.impl;

import com.kuang.message.entity.vo.MessageVo;
import com.kuang.message.service.InfoFriendFeedService;
import com.kuang.message.service.InfoIndexService;
import com.kuang.message.service.InfoMyNewsService;
import com.kuang.message.service.InfoReplyMeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Future;

@Service
@Slf4j
public class InfoIndexServiceImpl implements InfoIndexService {

    @Resource
    private InfoMyNewsService myNewsService;

    @Resource
    private InfoFriendFeedService friendFeedService;

    @Resource
    private InfoReplyMeService replyMeService;

    //查询我的消息、好友动态、回复我的
    @Async
    @Override
    public Future<MessageVo> findMFR(MessageVo messageVo, String userId) {
        Integer myNewsNumber = myNewsService.findUserUnreadNumber(userId);
        Integer friendFeedNumber = friendFeedService.findUserUnreadNumber(userId);
        Integer replyNumber = replyMeService.findUserUnreadNumber(userId);
        messageVo.setMyNewsNumber(myNewsNumber);
        messageVo.setFriendFeedNumber(friendFeedNumber);
        messageVo.setReplyNumber(replyNumber);
        return new AsyncResult<>(messageVo);
    }
}
