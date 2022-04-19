package com.kuang.oss.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Future;


public interface OssService {

    //上传图片接口
    String uploadPicture(MultipartFile picture);
    //上传文件接口
    String uploadFile(MultipartFile file);
    //下载文件
    void downloadFile(String fileSourceId , String name , HttpServletResponse response);

    //计算文件大小
    String getFileLength(MultipartFile file);
}
