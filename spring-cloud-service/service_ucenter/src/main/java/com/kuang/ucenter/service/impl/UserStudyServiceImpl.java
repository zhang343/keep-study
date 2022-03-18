package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.ucenter.entity.UserStudy;
import com.kuang.ucenter.mapper.UserStudyMapper;
import com.kuang.ucenter.service.UserStudyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Service
@Slf4j
public class UserStudyServiceImpl extends ServiceImpl<UserStudyMapper, UserStudy> implements UserStudyService {

    //查询用户历史记录，如果有true，没有false
    @Override
    public boolean findStudyByCourseIdAndUserId(String courseId, String userId) {
        log.info("查询用户历史记录，课程id：" + courseId + ",用户id：" + userId);
        QueryWrapper<UserStudy> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id" , courseId);
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper) != 0;
    }

}
