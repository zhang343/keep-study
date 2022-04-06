package com.kuang.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.Future;

public interface VodService {

    //上传视频到阿里云
    String uploadAlyVideo(MultipartFile file);

    //删除视频
    void removeAlyVideo(String id);

    //删除多个视频
    void removeAlyVideo(List<String> videoIdList);

    //根据视频id获取视频凭证
    String getPlayAuth(String videoSourceId);

    //获取视频时长
    Future<Long> getVideoTime(MultipartFile video);
}
