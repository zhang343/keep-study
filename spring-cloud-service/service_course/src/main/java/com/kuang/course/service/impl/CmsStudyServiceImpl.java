package com.kuang.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.course.entity.CmsStudy;
import com.kuang.course.entity.vo.CourseStudyVo;
import com.kuang.course.mapper.CmsStudyMapper;
import com.kuang.course.service.CmsStudyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-23
 */
@Service
public class CmsStudyServiceImpl extends ServiceImpl<CmsStudyMapper, CmsStudy> implements CmsStudyService {


    //增加学习记录
    @Async
    @Override
    public void addUserStudy(String userId, String courseId) {
        boolean userStudy = findUserStudy(userId, courseId);
        CmsStudy study = new CmsStudy();
        if(userStudy){
            QueryWrapper<CmsStudy> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id" , userId);
            wrapper.eq("course_id" , courseId);
            baseMapper.update(study , wrapper);
        }else {
            study.setUserId(userId);
            study.setCourseId(courseId);
            baseMapper.insert(study);
        }
    }

    //查询是否有该历史记录
    @Override
    public boolean findUserStudy(String userId, String courseId) {
        QueryWrapper<CmsStudy> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("course_id" , courseId);
        return baseMapper.selectCount(wrapper) != 0;
    }

    //查询用户学习数量
    @Override
    public Integer findUserStudyNumber(String userId) {
        QueryWrapper<CmsStudy> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查询用户学习记录
    @Override
    public List<CourseStudyVo> findUserStudy(String userId , Long current , Long limit) {
        current = (current - 1) * limit;
        return baseMapper.findUserStudy(userId , current , limit);
    }
}
