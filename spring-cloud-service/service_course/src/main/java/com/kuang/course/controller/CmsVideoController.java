package com.kuang.course.controller;


import com.kuang.course.client.VodClient;
import com.kuang.course.service.CmsCourseService;
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

/**
 * @author Xiaozhang
 * @since 2022-02-08
 * 小节处理类
 */
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
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询用户是否可以播放课程下面的视频不,视频id:" + id + ",用户id:" + userId);
        if(StringUtils.isEmpty(id) || StringUtils.isEmpty(videoSourceId) || userId == null){
            log.warn("有人进行非法查询是否可以播放指定视频,");
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Future<String> userAbility = videoService.findUserAbility(id, videoSourceId , userId);
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

