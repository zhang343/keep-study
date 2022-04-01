package com.kuang.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.course.entity.CmsCourse;
import com.kuang.course.entity.CmsVideo;
import com.kuang.course.mapper.CmsCourseMapper;
import com.kuang.course.mapper.CmsVideoMapper;
import com.kuang.course.service.CmsBillService;
import com.kuang.course.service.CmsVideoService;
import com.kuang.springcloud.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class CmsVideoServiceImpl extends ServiceImpl<CmsVideoMapper, CmsVideo> implements CmsVideoService {


    @Resource
    private CmsCourseMapper courseMapper;

    @Resource
    private CmsBillService billService;

    //通过课程id查找该课程下面小节数量
    @Override
    public Integer findVideoNumberByCourseId(String courseId) {
        QueryWrapper<CmsVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id" , courseId);
        return baseMapper.selectCount(wrapper);
    }

    //查找用户是否可以播放指定视频
    @Async
    @Override
    public Future<String> findUserAbility(String id , String videoSourceId , String userId) {
        CmsVideo cmsVideo = baseMapper.selectById(id);
        if(cmsVideo == null || !videoSourceId.equals(cmsVideo.getVideoSourceId())){
            return new AsyncResult<>(null);
        }
        String courseId = cmsVideo.getCourseId();
        CmsCourse course = courseMapper.selectById(courseId);
        if(course == null){
            return new AsyncResult<>(null);
        }
        int price = course.getPrice();
        //价格为0直接返回true,不为0再查账单
        if(price == 0){
            return new AsyncResult<>(courseId);
        }
        //查账单，看用户是否可用播放
        boolean flag = billService.findBillByCourseIdAndUserId(courseId, userId);
        if(flag){
            return new AsyncResult<>(courseId);
        }
        return new AsyncResult<>(null);
    }

    //缓存课程播放量
    @Async
    @Override
    public void setCourseViews(String courseId, String ip) {
        RedisUtils.setSet(courseId , ip);
        RedisUtils.expire(courseId , RedisUtils.COURSEVIEWTIME , TimeUnit.MINUTES);
        RedisUtils.setSet(RedisUtils.COURSE , courseId);
        RedisUtils.expire(RedisUtils.COURSE , RedisUtils.COURSEVIEWTIME , TimeUnit.MINUTES);
    }

}
