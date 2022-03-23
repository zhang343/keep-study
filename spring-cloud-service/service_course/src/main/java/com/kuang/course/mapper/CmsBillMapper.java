package com.kuang.course.mapper;

import com.kuang.course.entity.CmsBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.course.entity.vo.CourseStudyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
public interface CmsBillMapper extends BaseMapper<CmsBill> {

    //查询用户购买课程
    List<CourseStudyVo> findUserBuyCourse(@Param("userId") String userId,
                                          @Param("current") Long current,
                                          @Param("limit") Long limit);
}
