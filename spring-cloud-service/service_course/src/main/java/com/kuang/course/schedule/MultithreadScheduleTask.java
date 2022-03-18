package com.kuang.course.schedule;


import com.kuang.course.entity.CmsCourse;
import com.kuang.course.service.CmsCourseService;
import com.kuang.springcloud.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
/*
 * @author XiaoZhang
 * @date 2022/2/7 10:37
 * 定时任务类
 */
public class MultithreadScheduleTask {

    @Resource
    private CmsCourseService courseService;

    //每五分钟执行一次
    @Async
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void setCourseViews(){
        String treadName = Thread.currentThread().getName();
        log.info("开始执行定时任务,将缓存到redis中的课程播放量同步到数据库中,当前线程名" + treadName +"当前时间:" + LocalTime.now());
        log.info(treadName + "开始尝试去获取课程全局锁");
        boolean flag = RedisUtils.tryCourseLock(240);
        if(flag){
            log.info(treadName + "获取课程全局锁成功");
            //获取这段时间被访问的课程
            Set<Object> setAll = RedisUtils.getSetAll(RedisUtils.COURSE);
            RedisUtils.delKey(RedisUtils.COURSE);
            List<CmsCourse> courseList = new ArrayList<>();
            for(Object o : setAll){
                String courseId = (String) o;
                long setSize = RedisUtils.getSetSize(courseId);
                //可能会造成一些播放量的损失
                RedisUtils.delKey(courseId);
                if(setSize != 0){
                    CmsCourse course = new CmsCourse();
                    course.setId(courseId);
                    course.setViews(setSize);
                    courseList.add(course);
                }
            }
            log.info(treadName + "开始去更新课程播放量");
            if(courseList.size() != 0){
                courseService.updateCourseViews(courseList);
            }
            RedisUtils.unCourseLock();
        }else {
            log.warn(treadName + "获取课程全局锁失败");
        }
    }


    //缓存前三名课程
    @Async
    @Scheduled(fixedDelay = 10000)  //间隔10秒
    public void first(){
        log.info("定时任务开始, 缓存前三名课程,当前时间:" + LocalDateTime.now().toLocalTime());
        courseService.findCourseOrderByPrice();
    }
}
