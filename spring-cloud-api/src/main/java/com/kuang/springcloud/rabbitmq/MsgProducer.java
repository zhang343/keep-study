package com.kuang.springcloud.rabbitmq;

import com.kuang.springcloud.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MsgProducer implements RabbitTemplate.ConfirmCallback {

    //由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
    private RabbitTemplate rabbitTemplate;

    //注入rabbitTemplate
    @Autowired
    public MsgProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }


    //当我们回复了一个消息，则发送到用户消息中
    public void sendReplyMeMsg(String content) {
        CorrelationData correlationId = new CorrelationData(UUIDUtil.getUUID());
        //发送用户消息
        rabbitTemplate.convertAndSend(RabbitConfig.ReplyMeExchange, "", content, correlationId);
    }

    //当用户购买了一个课程，则发送消息到我的消息中
    public void sendMyNews(String content){
        CorrelationData correlationId = new CorrelationData(UUIDUtil.getUUID());
        //发送我的消息
        rabbitTemplate.convertAndSend(RabbitConfig.MyNewsExchange, "", content, correlationId);
    }

    //当用户发布了一个文章，则发送消息到好友动态中
    public void sendFriendFeed(String content){
        CorrelationData correlationId = new CorrelationData(UUIDUtil.getUUID());
        //发送好友动态
        rabbitTemplate.convertAndSend(RabbitConfig.FriendFeedExchange, "", content, correlationId);
    }



    //回调
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("回调id:" + correlationData);
        if (ack) {
            log.info("消息成功消费，回调id:" + correlationData);
        } else {
            log.info("消息消费失败:" + cause);
        }
    }
}
