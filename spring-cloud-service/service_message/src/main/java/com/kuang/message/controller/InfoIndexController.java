package com.kuang.message.controller;

import com.kuang.message.entity.vo.MessageVo;
import com.kuang.message.service.InfoCourseService;
import com.kuang.message.service.InfoIndexService;
import com.kuang.message.service.InfoSystemService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/message/index")
@Slf4j
public class InfoIndexController {

    @Resource
    private InfoIndexService infoIndexService;

    @Resource
    private InfoSystemService systemService;

    @Resource
    private InfoCourseService courseService;

    //查询各种未读消息数量
    @GetMapping("findAllNumber")
    public R findAllNumber(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询用户各种未读消息数量,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        //这里要查5个表，速度较慢，前三个表异步查询，后两张表不异步，等待前面异步查询
        MessageVo messageVo = new MessageVo();
        //查询我的消息、好友动态、回复我的
        Future<MessageVo> mfr = infoIndexService.findMFR(messageVo, userId);
        Integer systemNumber = systemService.findUserUnreadNumber(userId);
        Integer courseNumber = courseService.findUserUnreadNumber(userId);
        try {
            //等待0.1秒
            mfr.get(100 , TimeUnit.MILLISECONDS);
        }catch(Exception e){
            log.warn("查询我的消息、好友动态、回复我的失败");
        }

        messageVo.setSystemNumber(systemNumber);
        messageVo.setCourseNumber(courseNumber);
        Integer total = systemNumber + courseNumber + messageVo.getMyNewsNumber()
                + messageVo.getReplyNumber() + messageVo.getFriendFeedNumber();
        return R.ok().data("total" , total).data("message" , messageVo);
    }
}
