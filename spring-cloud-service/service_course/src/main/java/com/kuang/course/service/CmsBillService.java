package com.kuang.course.service;

import com.kuang.course.entity.CmsBill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.course.entity.vo.CourseStudyVo;

import java.util.List;


public interface CmsBillService extends IService<CmsBill> {

    //查找出用户是否购买了这个课程,true表示购买,false表示未购买
    boolean findBillByCourseIdAndUserId(String courseId, String userId);

    //查询用户购买课程数量
    Integer findUserBillNumber(String userId);

    //查询用户购买课程
    List<CourseStudyVo> findUserBuyCourse(String userId, Long current, Long limit);
}
