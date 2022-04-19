package com.kuang.message.mapper;

import com.kuang.message.entity.InfoCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.springcloud.entity.MessageCourseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface InfoCourseMapper extends BaseMapper<InfoCourse> {

    //查找课程通知
    List<String> findUserCourseList(@Param("current") Long current,
                                        @Param("limit") Long limit,
                                        @Param("userId") String userId);


    //让课程通知已读
    Integer setCourseRead(@Param("courseIdList") List<String> courseIdList,
                          @Param("userId") String userId);

}
