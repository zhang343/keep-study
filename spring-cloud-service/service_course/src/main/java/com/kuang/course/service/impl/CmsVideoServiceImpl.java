package com.kuang.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.course.client.VodClient;
import com.kuang.course.entity.CmsCourse;
import com.kuang.course.entity.CmsVideo;
import com.kuang.course.mapper.CmsCourseMapper;
import com.kuang.course.mapper.CmsVideoMapper;
import com.kuang.course.service.CmsBillService;
import com.kuang.course.service.CmsVideoService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Resource
    private VodClient vodClient;

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

    //上传小节
    @Transactional
    @Override
    public void uploadChapterVideo(CmsVideo video) {
        CmsCourse course = courseMapper.selectById(video.getCourseId());
        Integer totalLength = course.getTotalLength() + video.getLength();

        CmsCourse courseUpdate = new CmsCourse();
        courseUpdate.setId(course.getId());
        courseUpdate.setTotalLength(totalLength);
        courseUpdate.setVersion(course.getVersion());
        int i = courseMapper.updateById(courseUpdate);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "上传小节失败");
        }
        int insert = baseMapper.insert(video);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "上传小节失败");
        }
    }

    //删除小节
    @Transactional
    @Override
    public void deleteVideo(String videoId , String token) {
        CmsVideo video = baseMapper.selectById(videoId);
        CmsCourse course = courseMapper.selectById(videoId);
        Integer totalLength = course.getTotalLength() - video.getLength();

        //更新课程时长
        CmsCourse courseUpdate = new CmsCourse();
        courseUpdate.setId(course.getId());
        courseUpdate.setTotalLength(totalLength);
        courseUpdate.setVersion(course.getVersion());
        int i = courseMapper.updateById(courseUpdate);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "删除小节失败");
        }

        //删除视频
        int i1 = baseMapper.deleteById(videoId);
        if(i1 != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "删除小节失败");
        }

        //远程调用删除视频
        vodClient.deleteVideo(video.getVideoSourceId() , token);
    }

}
