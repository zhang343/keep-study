package com.kuang.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.message.entity.InfoCourse;
import com.kuang.message.mapper.InfoCourseMapper;
import com.kuang.message.service.InfoCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Service
public class InfoCourseServiceImpl extends ServiceImpl<InfoCourseMapper, InfoCourse> implements InfoCourseService {

    //查询未读消息
    @Override
    public Integer findUserUnreadNumber(String userId) {
        QueryWrapper<InfoCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("is_read" , 0);
        return baseMapper.selectCount(wrapper);
    }
}
