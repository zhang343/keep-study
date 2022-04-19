package com.kuang.course.mapper;

import com.kuang.course.entity.CmsCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.course.entity.vo.CourseVo;
import com.kuang.course.entity.vo.IndexCourseVo;
import com.kuang.springcloud.entity.BbsCourseVo;
import com.kuang.springcloud.entity.MessageCourseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CmsCourseMapper extends BaseMapper<CmsCourse> {

    //通过二级分类查找课程id
    List<IndexCourseVo> findCourseByTcId(String tcId);

    //查找课程详细信息
    CourseVo findCourseVo(String courseId);

    //更新课程浏览量
    void updateCourseViews(@Param("courseList") List<CmsCourse> courseList);

    //为消息模块服务，查询课程
    List<MessageCourseVo> findMessageCourseVo(@Param("courseIdList") List<String> courseIdList);
}
