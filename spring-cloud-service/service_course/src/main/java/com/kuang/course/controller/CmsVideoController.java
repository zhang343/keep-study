package com.kuang.course.controller;


import com.kuang.course.client.VodClient;
import com.kuang.course.service.CmsStudyService;
import com.kuang.course.service.CmsVideoService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/cms/video")
@Slf4j
public class CmsVideoController {

    @Resource
    private CmsVideoService videoService;

    @Resource
    private CmsStudyService studyService;

    @Resource
    private VodClient vodClient;

    //查找用户是否可以播放指定视频，并将视频播放凭证返回
    @GetMapping("getPlayAuth")
    public R findUserAbility(String id , String videoSourceId , HttpServletRequest request){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(id) || StringUtils.isEmpty(videoSourceId) || userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        //判断是否可以播放视频
        Future<String> userAbility = videoService.findUserAbility(id, videoSourceId , userId);
        //获取播放凭证
        R playAuthR = vodClient.getPlayAuth(videoSourceId);
        if(!playAuthR.getSuccess()){
            throw new XiaoXiaException(ResultCode.ERROR , "播放视频失败");
        }
        String playAuth = (String) playAuthR.getData().get("playAuth");

        String courseId = null;
        try {
            //等待0.3秒
            courseId = userAbility.get(300 , TimeUnit.MILLISECONDS);
        } catch(Exception e) {
            log.error("根据条件查询是否可用播放失败");
        }

        if(courseId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先购买够观看");
        }


        studyService.addUserStudy(userId , courseId);
        videoService.setCourseViews(courseId , request.getRemoteAddr());
        return R.ok().data("playAuth" , playAuth);
    }
}

