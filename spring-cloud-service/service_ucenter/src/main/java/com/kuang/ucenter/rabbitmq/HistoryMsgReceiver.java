package com.kuang.ucenter.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.springcloud.entity.UserStudyVo;
import com.kuang.springcloud.rabbitmq.RabbitConfig;
import com.kuang.ucenter.entity.UserStudy;
import com.kuang.ucenter.service.UserStudyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RabbitListener(queues = RabbitConfig.HistoryQUEUE)
@Slf4j
public class HistoryMsgReceiver {

    @Resource
    private UserStudyService studyService;

    @RabbitHandler
    public void process(String content) {
        try {
            log.info("开始在用户学习足迹里面插入数据,学习足迹:" + content);
            UserStudyVo userStudyVo = JSON.parseObject(content, UserStudyVo.class);
            boolean flag = studyService.findStudyByCourseIdAndUserId(userStudyVo.getCourseId() , userStudyVo.getUserId());
            UserStudy userStudy = new UserStudy();
            if(!flag){
                BeanUtils.copyProperties(userStudyVo , userStudy);
                studyService.save(userStudy);
            }else {
                //这里应该更新修改时间
                QueryWrapper<UserStudy> wrapper = new QueryWrapper<>();
                wrapper.eq("course_id" , userStudyVo.getCourseId());
                wrapper.eq("user_id" , userStudyVo.getUserId());
                studyService.update(userStudy , wrapper);
            }
        }catch(Exception e){
            log.warn("用户学习足迹数据插入失败,学习足迹:" + content);
        }
    }
}
