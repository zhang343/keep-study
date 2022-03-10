package com.kuang.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.course.entity.CmsCourse;
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
public interface CmsCourseService extends IService<CmsCourse> {

    //通过二级分类id查找课程
    List<IndexCourseVo> findCourseByTcId(String tcId);

    //查找课程详细信息
    CourseVo findCourseVo(String courseId);

    //查找课程价格
    Integer findCoursePrice(String courseId);

    //用户购买课程,并且返回课程标题
    String buyCourse(String courseId, String userId);

    //查找价格为前三的课程
    List<BbsCourseVo> findCourseOrderByPrice();

    //查找课程相关信息，为消息模块服务
    List<MessageCourseVo> findMessageCourseDetaile(List<String> courseIdList);

    //统计课程浏览量
    void statisticalCourseViews(List<MessageCourseVo> messageCourseVos);
}
