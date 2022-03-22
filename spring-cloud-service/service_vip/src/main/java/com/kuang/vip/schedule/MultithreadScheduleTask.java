package com.kuang.vip.schedule;

import com.kuang.vip.entity.Members;
import com.kuang.vip.mapper.MembersMapper;
import com.kuang.vip.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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
    private CacheService cacheService;

    @Resource
    private MembersMapper membersMapper;

    //定时任务,删除vip会员表中过期的会员,每一个小时执行一次
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void deleteVipMember(){
        log.info("开始执行定时任务,删除vip会员表中过期会员,当前时间为:" + LocalDateTime.now());
        List<Members> allVipMember = membersMapper.selectList(null);
        List<String> userIdList = new ArrayList<>();
        Date date = new Date();
        for(Members members : allVipMember){
            if(date.compareTo(members.getExpirationTime()) >= 0){
                userIdList.add(members.getId());
            }
        }

        if(userIdList.size() != 0){
            membersMapper.deleteBatchIds(userIdList);
        }
    }

    //缓存权益数据和vip成员
    @Async
    @Scheduled(fixedDelay = 10000)  //间隔10秒
    public void first(){
        log.info("定时任务开始,开始缓存权益数据和vip成员,当前时间:" + LocalDateTime.now().toLocalTime());
        cacheService.CacheNotVipRight();
        cacheService.CacheAllRightRedisList();
        cacheService.CacheAllRightRedisTreeMap();
        cacheService.CacheAllMembersRedisTreeMap();
    }
}
