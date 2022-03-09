package com.kuang.vip.schedule;

import com.kuang.vip.entity.Members;
import com.kuang.vip.service.MembersService;
import com.kuang.vip.service.UserTodayRightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
/*
 * @author XiaoZhang
 * @date 2022/2/7 10:37
 * 定时任务类
 */
public class MultithreadScheduleTask {

    @Resource
    private MembersService membersService;


    @Resource
    private UserTodayRightService userTodayRightService;

    //定时任务,删除vip会员表中过期的会员
    @Scheduled(cron = "0 0 1 * * ?")
    public void deleteVipMember(){
        log.info("开始执行定时任务,删除vip会员表中过期会员,当前时间为:" + LocalDateTime.now());
        List<Members> allVipMember = membersService.findAllVipMember();
        List<String> userIdList = new ArrayList<>();
        Date date = new Date();
        for(Members members : allVipMember){
            if(date.compareTo(members.getExpirationTime()) >= 0){
                userIdList.add(members.getId());
            }
        }


        try {
            membersService.deleteMemberByIdList(userIdList);
        }catch(Exception e){
            log.warn("删除vip会员表中过期会员失败，请检查数据库");
        }
    }


    //定时任务,更新用户每日权益
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateMemberTodayRight(){
        log.info("开始执行定时任务,更新用户每日权益,当前时间为:" + LocalDateTime.now());
        try {
            userTodayRightService.updateMemberTodayRight();
        }catch(Exception e){
            log.warn("更新用户每日权益失败，请检查数据库");
        }
    }
}
