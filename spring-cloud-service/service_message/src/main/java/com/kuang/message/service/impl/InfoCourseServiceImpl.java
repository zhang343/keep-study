package com.kuang.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.message.client.CourseClient;
import com.kuang.message.entity.InfoCourse;
import com.kuang.message.mapper.InfoCourseMapper;
import com.kuang.message.service.InfoCourseService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;


@Service
@Slf4j
public class InfoCourseServiceImpl extends ServiceImpl<InfoCourseMapper, InfoCourse> implements InfoCourseService {


    @Resource
    private CourseClient courseClient;

    //查询未读消息
    @Override
    public Integer findUserUnreadNumber(String userId) {
        QueryWrapper<InfoCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("is_read" , 0);
        return baseMapper.selectCount(wrapper);
    }

    //查找课程通知数量
    @Override
    public Integer findUserNewsNumber(String userId) {
        QueryWrapper<InfoCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查找课程通知
    @Override
    public List<String> findUserNewsId(Long current, Long limit, String userId) {
        current = (current - 1) * limit;
        return baseMapper.findUserCourseList(current , limit , userId);
    }

    //让课程通知已读
    @Async
    @Override
    public void setCourseRead(List<String> courseIdList, String userId) {
        if(courseIdList.size() != 0){
            baseMapper.setCourseRead(courseIdList , userId);
        }
    }

    //远程调用查询课程
    @Async
    @Override
    public Future<Object> findMessageCourseVos(List<String> courseIdList) {
        R messageCourseVo = courseClient.findMessageCourseVo(courseIdList);
        if(!messageCourseVo.getSuccess()){
            throw new RuntimeException();
        }
        return new AsyncResult<>(messageCourseVo.getData().get("messageCourseVos"));
    }
}
