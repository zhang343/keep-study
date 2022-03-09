package com.kuang.vod.controller;


import com.alibaba.fastjson.JSON;
import com.kuang.springcloud.entity.UserStudyVo;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.rabbitmq.MsgProducer;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vod.client.CourseClient;
import com.kuang.vod.service.VodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/vod/video")
@Slf4j
public class VodController {

    @Resource
    private VodService vodService;

    @Resource
    private CourseClient courseClient;

    @Resource
    private MsgProducer msgProducer;


    //根据视频id获取视频凭证
    @GetMapping("getPlayAuth")
    public R getPlayAuth(String videoId , HttpServletRequest request) {
        log.info("开始查询视频播放凭证，视频id：" + videoId);
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(videoId) || userId == null){
            log.warn("有人非法操作,视频id:" + videoId + ",用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        log.info("远程调用service-course服务下面的/cms/video/findUserAbility接口，查看用户是否可以获取视频播放凭证");
        R result = courseClient.findUserAbility(videoId);
        if(!result.getSuccess() || !(boolean) result.getData().get("isAbility")){
            log.warn("用户没有权限播放视频,视频id:" + videoId + ",用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "请购买后观看");
        }
        String history = (String) result.getData().get("userStudyVo");
        UserStudyVo userStudyVo = JSON.parseObject(history , UserStudyVo.class);
        String videoSourceId = userStudyVo.getVideoSourceId();
        String playAuth = vodService.getPlayAuth(videoSourceId);
        try {
            log.info("开始向rabbitmq发送数据,存储用户学习记录,用户id:" + userId);
            msgProducer.sendHistoryMsg(history);
        }catch(Exception e){
            log.warn("向rabbitmq发送数据失败,存储用户学习记录,用户id:" + userId);
        }
        return R.ok().data("playAuth" , playAuth);
    }

}
