package com.kuang.course.mapper;

import com.kuang.course.entity.CmsStudy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.course.entity.vo.CourseStudyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CmsStudyMapper extends BaseMapper<CmsStudy> {

    //查询用户学习记录
    List<CourseStudyVo> findUserStudy(@Param("userId") String userId,
                                      @Param("current") Long current,
                                      @Param("limit") Long limit);
}
