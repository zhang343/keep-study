package com.kuang.course.mapper;

import com.kuang.course.entity.CmsCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.course.entity.vo.CourseVo;
import com.kuang.course.entity.vo.IndexCourseVo;
import com.kuang.springcloud.entity.BbsCourseVo;
import com.kuang.springcloud.entity.MessageCourseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
public interface CmsCourseMapper extends BaseMapper<CmsCourse> {

    //通过二级分类查找课程id
    List<IndexCourseVo> findCourseByTcId(String tcId);

    //查找课程详细信息
    CourseVo findCourseVo(String courseId);

    //查找价格为前三的课程
    List<BbsCourseVo> findCourseOrderByPrice();

    //查找课程相关信息，为消息模块服务
    List<MessageCourseVo> findMessageCourseDetaile(@Param("courseIdList") List<String> courseIdList);

    //更新课程浏览量
    Long updateCourseViews(@Param("courseList") List<CmsCourse> courseList);
}
