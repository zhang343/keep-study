package com.kuang.course.schedule;


import com.kuang.course.entity.CmsCourse;
import com.kuang.course.service.CmsCourseService;
import com.kuang.springcloud.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalTime;
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
    private CmsCourseService courseService;

    //每五分钟执行一次
    @Async
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void setCourseViews(){
        String treadName = Thread.currentThread().getName();
        log.info("开始执行定时任务,将缓存到redis中的课程播放量同步到数据库中,当前线程名" + treadName +"当前时间:" + LocalTime.now());
        log.info(treadName + "开始尝试去获取课程全局锁");
        boolean flag = RedisUtils.tryCourseLock(360);
        if(flag){
            log.info(treadName + "获取课程全局锁成功");
            List<CmsCourse> allCourse = courseService.findAllCourse();
            List<CmsCourse> courseList = new ArrayList<>();
            for(CmsCourse course : allCourse){
                long setSize = RedisUtils.getSetSize(course.getId());
                if(setSize != 0){
                    course.setViews(course.getViews() + setSize);
                    courseList.add(course);
                    //从上面到这里删除，可能会损失一点播放量
                    RedisUtils.delKey(course.getId());
                }
            }
            log.info(treadName + "开始去更新课程播放量");
            if(courseList.size() != 0){
                courseService.updateBatchById(courseList);
            }
            RedisUtils.unCourseLock();
        }else {
            log.warn(treadName + "获取文章全局锁失败");
        }
    }
}
