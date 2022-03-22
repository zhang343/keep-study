package com.kuang.ucenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.ucenter.entity.UserStudy;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserStudyService extends IService<UserStudy> {

    //查询用户历史记录，如果有true，没有false
    boolean findStudyByCourseIdAndUserId(String courseId , String userId);

    //查找用户学习数量
    Integer findUserStudyNumber(String userId);
}
