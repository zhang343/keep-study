package com.kuang.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.course.entity.CmsVideo;

import java.util.concurrent.Future;


public interface CmsVideoService extends IService<CmsVideo> {

    //通过课程id查找该课程下面小节数量
    Integer findVideoNumberByCourseId(String courseId);

    //查找用户是否可以播放指定视频
    Future<String> findUserAbility(String id, String videoSourceId , String userId);

    //缓存课程播放量
    void setCourseViews(String courseId, String ip);

    //上传小节
    void uploadChapterVideo(CmsVideo video);

    //删除小节
    void deleteVideo(String videoId , String token);
}
