package com.kuang.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vod.service.VodService;
import com.kuang.vod.utils.ConstantVodUtils;
import com.kuang.vod.utils.InitVodCilent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;

@Service
@Slf4j
public class VodServiceImpl implements VodService {

    //上传视频到阿里云
    @Override
    public String uploadAlyVideo(MultipartFile file) {
        log.info("上传视频到阿里云，视频原名称：" + file.getOriginalFilename());
        //获取title
        String fileName = file.getOriginalFilename();
        String title = fileName.substring(0, fileName.lastIndexOf("."));
        //获取文件输入流
        InputStream inputStream = null;
        try {
            log.info("获取视频输入流，视频原名称：" + fileName);
            inputStream = file.getInputStream();
        } catch(IOException e) {
            log.error("获取视频输入流失败，视频原名称：" + fileName);
            throw new XiaoXiaException(ResultCode.ERROR, "失败");
        }
        //创建上传对象
        log.info("开始上传视频，视频原名称：" + fileName);
        UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);
        //上传
        UploadVideoImpl uploader = new UploadVideoImpl();
        //获取上传之后的响应
        UploadStreamResponse response = uploader.uploadStream(request);
        return response.getVideoId();
    }

    
    //删除视频
    @Override
    public void removeAlyVideo(String id) {
        log.info("开始删除视频，视频id：" + id);
        //初始化对象
        DefaultAcsClient client = InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
        //创建删除视频request对象
        DeleteVideoRequest request = new DeleteVideoRequest();
        //向request设置视频id
        request.setVideoIds(id);
        //调用初始化对象的方法实现删除
        try {
            log.info("删除视频，视频id：" + id);
            client.getAcsResponse(request);
        } catch(ClientException e) {
            log.error("删除视频失败，视频id：" + id);
            throw new XiaoXiaException(ResultCode.ERROR, "删除视频失败");
        }
    }

    //删除多个视频
    @Override
    public void removeAlyVideo(List<String> videoIdList) {
        log.info("开始删除多个视频，视频id：" + videoIdList);
        //初始化对象
        DefaultAcsClient client = InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
        //创建删除视频request对象
        DeleteVideoRequest request = new DeleteVideoRequest();
        //设置删除视频id
        String videoIds = StringUtils.join(videoIdList.toArray() , ",");
        ;//向request设置视频id
        request.setVideoIds(videoIds);
        //调用初始化对象的方法实现删除
        try {
            log.info("删除多个视频，视频id：" + videoIdList);
            client.getAcsResponse(request);
        } catch(ClientException e) {
            log.error("删除多个视频失败，视频id：" + videoIdList);
            throw new XiaoXiaException(ResultCode.ERROR , "失败");
        }
    }

    //根据视频id获取视频凭证
    @Async
    @Override
    public Future<String> getPlayAuth(String id) {
        log.info("开始获取视频播放凭证，视频id为：" + id);
        //创建初始化对象
        DefaultAcsClient client =
                InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
        //创建获取凭证request和response对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        //向request设置视频id
        request.setVideoId(id);
        //调用方法得到凭证
        GetVideoPlayAuthResponse response = null;
        try {
            log.info("获取视频播放凭证，视频id为：" + id);
            response = client.getAcsResponse(request);
        } catch(ClientException e) {
            log.error("获取视频播放凭证失败，视频id为：" + id);
            throw new XiaoXiaException(ResultCode.ERROR , "获取视频播放凭证失败");
        }
        return AsyncResult.forValue(response.getPlayAuth());
    }
}
