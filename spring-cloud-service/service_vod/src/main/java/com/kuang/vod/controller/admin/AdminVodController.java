package com.kuang.vod.controller.admin;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vod.service.VodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/vod/admin")
@Slf4j
public class AdminVodController {


    @Resource
    private VodService vodService;

    //上传视频
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("uploadVideo")
    public R uploadOssFile(MultipartFile Video){
        Future<Long> videoTime = vodService.getVideoTime(Video);
        String videoSourceId = vodService.uploadAlyVideo(Video);
        Long aLong = 0L;
        try {
            aLong = videoTime.get();
        }catch(Exception e){
            throw new XiaoXiaException(ResultCode.ERROR , "上传失败");
        }
        return R.ok().data("videoSourceId" , videoSourceId).data("length" , aLong);
    }

    //删除视频
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("deleteVideo")
    public R deleteVideo(String videoSourceId){
        vodService.removeAlyVideo(videoSourceId);
        return R.ok();
    }

    //删除多个视频
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("deleteVideoByIdList")
    public R deleteVideoByIdList(String[] videoIdList){
        vodService.removeAlyVideo(Arrays.asList(videoIdList));
        return R.ok();
    }

}
