package com.kuang.vip.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.vip.entity.Members;
import com.kuang.vip.mapper.MembersMapper;
import com.kuang.vip.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@Slf4j
public class MultithreadScheduleTask {

    @Resource
    private CacheService cacheService;

    @Resource
    private MembersMapper membersMapper;

    //定时任务,删除vip会员表中过期的会员,每一个小时执行一次
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void deleteVipMember(){
        log.info("开始执行定时任务,删除vip会员表中过期会员,当前时间为:" + LocalDateTime.now());
        QueryWrapper<Members> wrapper = new QueryWrapper<>();
        wrapper.lt("expiration_time" , new Date());
        membersMapper.delete(wrapper);
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
