package com.kuang.vod.controller.inside;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vod.service.VodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inside/video")
@Slf4j
public class InsideVideoController {

    @Resource
    private VodService vodService;

    //根据视频id获取视频凭证
    @GetMapping("getPlayAuth")
    public R getPlayAuth(String videoSourceId) {
        if(StringUtils.isEmpty(videoSourceId)){
            throw new XiaoXiaException(ResultCode.ERROR , "远程视频id为空");
        }
        String playAuth = vodService.getPlayAuth(videoSourceId);
        return R.ok().data("playAuth" , playAuth);
    }

}
