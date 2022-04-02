package com.kuang.message.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.kuang.message.entity.InfoMyNews;
import com.kuang.message.service.InfoMyNewsService;
import com.kuang.springcloud.entity.InfoMyNewsVo;
import com.kuang.springcloud.rabbitmq.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RabbitListener(queues = RabbitConfig.MyNewsQUEUE)
@Slf4j
public class MyNewsMsgReceiver {



    @Resource
    private InfoMyNewsService infoMyNewsService;

    @RabbitHandler
    public void process(String content) {
        try {
            log.info("开始在我的消息插入数据,我的消息:" + content);
            InfoMyNewsVo infoMyNewsVo = JSON.parseObject(content, InfoMyNewsVo.class);
            InfoMyNews infoMyNews = new InfoMyNews();
            BeanUtils.copyProperties(infoMyNewsVo , infoMyNews);
            infoMyNewsService.save(infoMyNews);
        } catch(Exception e) {
            log.warn("开始在我的消息插入数据失败,我的消息:" + content);
        }
    }
}
