package com.kuang.ucenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.ucenter.entity.UserStudy;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserStudyService extends IService<UserStudy> {

    //查询用户历史记录，如果有true，没有false
    boolean findStudyByCourseIdAndUserId(String courseId , String userId);
}
