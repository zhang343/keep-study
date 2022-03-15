package com.kuang.vod.controller;


import com.kuang.springcloud.utils.R;
import com.kuang.vod.service.VodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/frontuservideo")
@Slf4j
public class VodController {

    @Resource
    private VodService vodService;


    //根据视频id获取视频凭证
    @GetMapping("getPlayAuth")
    public R getPlayAuth(String videoSourceId) {
        String playAuth = vodService.getPlayAuth(videoSourceId);
        return R.ok().data("playAuth" , playAuth);
    }

}
