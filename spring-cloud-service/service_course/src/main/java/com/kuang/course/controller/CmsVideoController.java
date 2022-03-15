package com.kuang.course.controller;


import com.alibaba.fastjson.JSON;
import com.kuang.course.service.CmsVideoService;
import com.kuang.springcloud.entity.UserStudyVo;
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

    //查找用户是否可以播放指定视频，并将云端视频id返回
    @GetMapping("findUserAbility")
    public R findUserAbility(String id , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询用户是否可以播放课程下面的视频不,视频id:" + id + ",用户id:" + userId);
        if(StringUtils.isEmpty(id) || userId == null){
            log.warn("有人进行非法查询是否可以播放指定视频,");
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Future<Boolean> userAbility = videoService.findUserAbility(id, userId);
        UserStudyVo userStudyVo = videoService.findUserStudyVoByCourseId(id);
        boolean isAbility = false;
        try {
            isAbility = userAbility.get();
        } catch(Exception e) {
            log.error("根据条件查询是否可用播放失败");
        }

        return R.ok().data("isAbility" , isAbility).data("userStudyVo" , JSON.toJSONString(userStudyVo));
    }
}

