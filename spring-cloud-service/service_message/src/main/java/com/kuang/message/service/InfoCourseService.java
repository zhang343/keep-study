package com.kuang.message.service;

import com.kuang.message.entity.InfoCourse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface InfoCourseService extends IService<InfoCourse> {

    //查询未读消息
    Integer findUserUnreadNumber(String userId);
}
