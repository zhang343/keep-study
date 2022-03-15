package com.kuang.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.course.entity.CmsCourse;
import com.kuang.course.entity.CmsVideo;
import com.kuang.course.mapper.CmsCourseMapper;
import com.kuang.course.mapper.CmsVideoMapper;
import com.kuang.course.service.CmsBillService;
import com.kuang.course.service.CmsCourseService;
import com.kuang.course.service.CmsVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.entity.MessageCourseVo;
import com.kuang.springcloud.entity.UserStudyVo;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
@Service
@Slf4j
public class CmsVideoServiceImpl extends ServiceImpl<CmsVideoMapper, CmsVideo> implements CmsVideoService {


    @Resource
    private CmsCourseMapper courseMapper;

    @Resource
    private CmsBillService billService;

    //通过课程id查找该课程下面小节数量
    @Cacheable(value = "videoNumber")
    @Override
    public Integer findVideoNumberByCourseId(String courseId) {
        log.info("通过课程id查找对应小节数量，课程id：" + courseId);
        QueryWrapper<CmsVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id" , courseId);
        return baseMapper.selectCount(wrapper);
    }

    //查找用户是否可以播放指定视频
    @Async
    @Override
    public Future<Boolean> findUserAbility(String id , String videoSourceId , String userId) {
        log.info("查询用户是否可以播放课程下面的视频不,视频id:" + id + ",用户id:" + userId);
        CmsVideo cmsVideo = baseMapper.selectById(id);
        if(cmsVideo == null || !videoSourceId.equals(cmsVideo.getVideoSourceId())){
            log.warn("该视频不存在,请不要非法操作,视频id:" + id);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        String courseId = cmsVideo.getCourseId();

        QueryWrapper<CmsCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("id" , courseId);
        wrapper.select("price");
        CmsCourse course = courseMapper.selectOne(wrapper);
        if(course == null){
            throw new XiaoXiaException(ResultCode.ERROR , "该课程不存在");
        }
        Integer price = course.getPrice();
        //价格为0直接返回true,不为0再查账单
        if(price == 0){
            return new AsyncResult<>(true);
        }
        //查账单，看用户是否可用播放
        boolean flag = billService.findBillByCourseIdAndUserId(courseId, userId);
        return new AsyncResult<>(flag);
    }

    //查询相关课程信息
    @Override
    public UserStudyVo findUserStudyVoByCourseId(String id) {
        log.info("查询视频云端id和相关课程信息,视频id:" + id);
        UserStudyVo userStudyVoByCourseId = baseMapper.findUserStudyVoByCourseId(id);
        if(userStudyVoByCourseId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        return userStudyVoByCourseId;
    }

    //设置课程小节数量
    @Async
    @Override
    public Future<List<MessageCourseVo>> findVideoNumberByCourseId(List<MessageCourseVo> messageCourseVos) {
        for(MessageCourseVo messageCourseVo : messageCourseVos){
            Integer videoNumberByCourseId = findVideoNumberByCourseId(messageCourseVo.getCourseId());
            messageCourseVo.setVideoNumber(videoNumberByCourseId);
        }
        return new AsyncResult<>(messageCourseVos);
    }

    //缓存课程播放量
    @Async
    @Override
    public void setCourseViews(String id, String ip) {
        CmsVideo cmsVideo = baseMapper.selectById(id);
        if(cmsVideo == null){
            return;
        }
        String courseId = cmsVideo.getCourseId();
        RedisUtils.setSet(courseId , ip);
        RedisUtils.expire(courseId , RedisUtils.COURSEVIEWTIME , TimeUnit.MINUTES);
    }

}
