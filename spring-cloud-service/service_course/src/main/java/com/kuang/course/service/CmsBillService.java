package com.kuang.course.service;

import com.kuang.course.entity.CmsBill;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
public interface CmsBillService extends IService<CmsBill> {

    //查找出用户是否购买了这个课程,true表示购买,false表示未购买
    boolean findBillByCourseIdAndUserId(String courseId, String userId);

    //查询用户购买课程数量
    Integer findUserBillNumber(String userId);
}
