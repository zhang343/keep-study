package com.kuang.oss.controller;


import com.kuang.oss.service.OssService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author XiaoZhang
 * @date 2022/2/3 14:32
 * 图片处理类
 */
@RestController
@RequestMapping("/oss/picture")
@Slf4j
public class OssPictureController {

    @Resource
    private OssService ossService;

    //上传单个图片接口
    @PostMapping("uploadPicture")
    public R uploadOssFile(MultipartFile picture){
        String url = ossService.uploadPicture(picture);
        return R.ok().data("url" , url);
    }

}
