package com.kuang.course.service;

import com.kuang.course.entity.CmsStudy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.course.entity.vo.CourseStudyVo;

import java.util.List;


public interface CmsStudyService extends IService<CmsStudy> {

    //增加学习记录
    void addUserStudy(String userId, String courseId);

    //查询是否有该历史记录
    boolean findUserStudy(String userId, String courseId);

    //查询用户学习数量
    Integer findUserStudyNumber(String userId);

    //查询用户学习记录
    List<CourseStudyVo> findUserStudy(String userId , Long current , Long limit);
}
