package com.kuang.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.course.entity.CmsVideo;

import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
public interface CmsVideoService extends IService<CmsVideo> {

    //通过课程id查找该课程下面小节数量
    Integer findVideoNumberByCourseId(String courseId);

    //查找用户是否可以播放指定视频
    Future<String> findUserAbility(String id, String videoSourceId , String userId);

    //缓存课程播放量
    void setCourseViews(String courseId, String ip);
}
