package com.kuang.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.course.entity.CmsCourse;
import com.kuang.course.entity.vo.CourseVo;
import com.kuang.course.entity.vo.IndexCourseVo;
import com.kuang.springcloud.entity.BbsCourseVo;
import com.kuang.springcloud.entity.MessageCourseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CmsCourseService extends IService<CmsCourse> {

    //通过二级分类id查找课程
    List<IndexCourseVo> findCourseByTcId(String tcId);

    //查找课程详细信息
    CourseVo findCourseVo(String courseId);

    //用户购买课程,并且返回课程标题
    String buyCourse(String courseId, String userId);

    //查找价格为前三的课程
    List<BbsCourseVo> findCourseOrderByPrice();

    //更新课程浏览量
    void updateCourseViews(List<CmsCourse> courseList);

    //为消息模块服务，查询课程
    List<MessageCourseVo> findMessageCourseVo(List<String> courseIdList);

    //查找课程播放量
    List<CmsCourse> findCourseViewsList(List<String> courseIdList);

    //用户购买课程成功，发送消息到rabbitmq
    void sendMyNews(String userId, String courseId, String title);
}
