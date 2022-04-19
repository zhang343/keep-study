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
public class MultithreadScheduleTask {

    @Resource
    private CmsCourseService courseService;

    //每五分钟执行一次
    @Async
    @Scheduled(cron = "0 0 0 * * ? ")
    public void setCourseViews(){
        String treadName = Thread.currentThread().getName();
        log.info("开始执行定时任务,将缓存到redis中的课程播放量同步到数据库中,当前线程名" + treadName +"当前时间:" + LocalTime.now());
        log.info(treadName + "开始尝试去获取课程全局锁");
        boolean flag = RedisUtils.tryCourseLock(240);
        if(flag){
            log.info(treadName + "获取课程全局锁成功");
            //获取这段时间被访问的课程
            Set<Object> setAll = RedisUtils.getSetAll(RedisUtils.COURSE);
            if(setAll == null || setAll.size() == 0){
                RedisUtils.unCourseLock();
                return;
            }
            RedisUtils.delKey(RedisUtils.COURSE);
            List<String> courseIdList = new ArrayList<>();
            for(Object o : setAll){
                courseIdList.add((String) o);
            }

            List<CmsCourse> courseList = courseService.findCourseViewsList(courseIdList);
            List<CmsCourse> courseUpdateList = new ArrayList<>();
            for(CmsCourse course : courseList){
                long setSize = RedisUtils.getSetSize(course.getId());
                RedisUtils.delKey(course.getId());
                if(setSize != 0){
                    course.setViews(course.getViews() + setSize);
                    courseUpdateList.add(course);
                }
            }

            log.info(treadName + "开始去更新课程播放量");
            if(courseUpdateList.size() != 0){
                courseService.updateCourseViews(courseUpdateList);
            }
            RedisUtils.unCourseLock();
        }else {
            log.warn(treadName + "获取课程全局锁失败");
        }
    }
}
