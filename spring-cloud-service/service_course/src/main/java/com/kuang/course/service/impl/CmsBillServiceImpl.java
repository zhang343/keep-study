package com.kuang.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.course.entity.CmsBill;
import com.kuang.course.entity.vo.CourseStudyVo;
import com.kuang.course.mapper.CmsBillMapper;
import com.kuang.course.service.CmsBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
@Service
@Slf4j
public class CmsBillServiceImpl extends ServiceImpl<CmsBillMapper, CmsBill> implements CmsBillService {

    //查找出用户是否购买了这个课程,true表示购买,false表示未购买
    @Override
    public boolean findBillByCourseIdAndUserId(String courseId, String userId) {
        log.info("查找课程是否被用户购买,课程id:" + courseId + ",用户id:" + userId);
        QueryWrapper<CmsBill> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("course_id" , courseId);
        return baseMapper.selectCount(wrapper) != 0;
    }

    //查询用户购买课程数量
    @Override
    public Integer findUserBillNumber(String userId) {
        log.info("查询用户购买课程数量,用户id:" + userId);
        QueryWrapper<CmsBill> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查询用户购买课程
    @Override
    public List<CourseStudyVo> findUserBuyCourse(String userId, Long current, Long limit) {
        current = (current - 1) * limit;
        return baseMapper.findUserBuyCourse(userId , current , limit);
    }
}
