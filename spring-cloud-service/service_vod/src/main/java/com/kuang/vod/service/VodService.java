package com.kuang.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {

    //上传视频到阿里云
    String uploadAlyVideo(MultipartFile file);

    //删除视频
    void removeAlyVideo(String id);

    //删除多个视频
    void removeAlyVideo(List<String> videoIdList);

    //根据视频id获取视频凭证
    String getPlayAuth(String videoSourceId);
}
