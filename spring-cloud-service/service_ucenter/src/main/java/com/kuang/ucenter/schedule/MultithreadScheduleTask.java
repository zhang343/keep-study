package com.kuang.ucenter.schedule;

import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Component
@Slf4j
/*
 * @author XiaoZhang
 * @date 2022/2/7 10:37
 * 定时任务类
 */
public class MultithreadScheduleTask {

    @Resource
    private UserInfoService userInfoService;

    //缓存系统用户数量
    @Async
    @Scheduled(fixedDelay = 10000)  //间隔10秒
    public void first(){
        log.info("定时任务开始, 缓存全部用户数量,当前时间:" + LocalDateTime.now().toLocalTime());
        userInfoService.findUserNumber();
    }

    //定时任务,更新用户每日签到权益，每天凌晨执行
    @Scheduled(cron = "0 0 0 * * ? ")
    public void updateUserIsSign(){
        log.info("开始执行定时任务,更新用户每日签到权益,当前时间为:" + LocalDateTime.now());
        userInfoService.updateUserIsSign();
    }
}
