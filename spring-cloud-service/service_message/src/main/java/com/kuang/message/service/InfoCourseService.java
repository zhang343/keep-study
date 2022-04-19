package com.kuang.message.service;

import com.kuang.message.entity.InfoCourse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.concurrent.Future;


public interface InfoCourseService extends IService<InfoCourse> {

    //查询未读消息
    Integer findUserUnreadNumber(String userId);

    //查找课程通知数量
    Integer findUserNewsNumber(String userId);

    //查找课程通知的课程id
    List<String> findUserNewsId(Long current, Long limit, String userId);

    //让课程通知已读
    void setCourseRead(List<String> courseIdList, String userId);

    //远程调用查询课程
    Future<Object> findMessageCourseVos(List<String> courseIdList);
}
