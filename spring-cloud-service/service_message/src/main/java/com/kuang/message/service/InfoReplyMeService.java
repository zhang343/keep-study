package com.kuang.message.service;

import com.kuang.message.entity.InfoReplyMe;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.message.entity.vo.ReplyMeVo;

import java.util.List;
import java.util.concurrent.Future;


public interface InfoReplyMeService extends IService<InfoReplyMe> {

    //查找未读消息
    Integer findUserUnreadNumber(String userId);

    //查询回复我的消息数量
    Integer findUserNewsNumber(String userId);

    //查询回复我的消息
    List<ReplyMeVo> findUserNews(Long current, Long limit, String userId);

    //设置回复我的消息已读
    void setReplyMeRead(List<ReplyMeVo> replyMeVos);

    //删除用户回复消息
    void delete(String id, String userId);

    //回复用户消息
    void addreply(String id, String content, String userId);
}
