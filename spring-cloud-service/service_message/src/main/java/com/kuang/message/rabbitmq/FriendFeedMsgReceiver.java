package com.kuang.message.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.kuang.message.entity.InfoFriendFeed;
import com.kuang.message.service.InfoFriendFeedService;
import com.kuang.springcloud.entity.InfoFriendFeedVo;
import com.kuang.springcloud.rabbitmq.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@RabbitListener(queues = RabbitConfig.FriendFeedQUEUE)
@Slf4j
public class FriendFeedMsgReceiver {

    @Resource
    private InfoFriendFeedService infoFriendFeedService;

    @RabbitHandler
    public void process(String content) {
        try {
            log.info("开始在好友动态插入数据,我的消息:" + content);
            InfoFriendFeedVo infoFriendFeedVo = JSON.parseObject(content , InfoFriendFeedVo.class);
            List<String> userIdList = infoFriendFeedVo.getUserIdList();
            List<InfoFriendFeed> infoFriendFeedList = new ArrayList<>();
            for(String userId : userIdList){
                InfoFriendFeed infoFriendFeed = new InfoFriendFeed();
                BeanUtils.copyProperties(infoFriendFeedVo , infoFriendFeed);
                infoFriendFeed.setUserId(userId);
                infoFriendFeedList.add(infoFriendFeed);
            }
            if(userIdList.size() != 0){
                infoFriendFeedService.saveBatch(infoFriendFeedList);
            }
        } catch(Exception e) {
            log.warn("开始在好友动态插入数据失败,我的消息:" + content);
        }
    }
}
