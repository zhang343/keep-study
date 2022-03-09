package com.kuang.message.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.kuang.message.entity.InfoReplyMe;
import com.kuang.message.service.InfoReplyMeService;
import com.kuang.springcloud.entity.InfoReplyMeVo;
import com.kuang.springcloud.rabbitmq.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RabbitListener(queues = RabbitConfig.ReplyMeQUEUE)
@Slf4j
public class ReplyMsgReceiver {



    @Resource
    private InfoReplyMeService infoReplyMeService;

    @RabbitHandler
    public void process(String content) {
        try {
            log.info("开始在用户回复我的里面插入数据,回复我的:" + content);
            InfoReplyMeVo infoReplyMeVo = JSON.parseObject(content, InfoReplyMeVo.class);
            InfoReplyMe infoReplyMe = new InfoReplyMe();
            BeanUtils.copyProperties(infoReplyMeVo , infoReplyMe);
            infoReplyMeService.save(infoReplyMe);
        }catch(Exception e){
            log.warn("用户回复我的里面插入数据失败,回复我的:" + content);
        }
    }
}
