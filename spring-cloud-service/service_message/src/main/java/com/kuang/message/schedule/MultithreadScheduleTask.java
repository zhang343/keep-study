package com.kuang.message.schedule;

import com.kuang.message.entity.InfoFriendFeed;
import com.kuang.message.service.InfoFriendFeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private InfoFriendFeedService infoFriendFeedService;


    //定时任务,更新好友动态中的文章浏览量
    @Scheduled(cron = "0 0 0,6,14,23 * * ?")
    public void updateMemberTodayRight(){
        log.info("开始执行定时任务,更新好友动态中的文章浏览量,当前时间为:" + LocalDateTime.now());
        try {
            List<String> articleIdList = new ArrayList<>();
            List<InfoFriendFeed> allFriendFeedList = infoFriendFeedService.findAllFriendFeed();
            for(InfoFriendFeed infoFriendFeed : allFriendFeedList){
                articleIdList.add(infoFriendFeed.getArticleId());
            }
            if(articleIdList.size() != 0){
                //尚未完善
            }
        }catch(Exception e){
            log.warn("更新好友动态中的文章浏览量失败，请检查数据库");
        }
    }
}
