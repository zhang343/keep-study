package com.kuang.message.controller;


import com.kuang.message.entity.vo.MyNewsVo;
import com.kuang.message.entity.vo.ReplyMeVo;
import com.kuang.message.service.InfoReplyMeService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@RestController
@RequestMapping("/message/reply")
@Slf4j
public class InfoReplyMeController {


    @Resource
    private InfoReplyMeService replyMeService;

    //查询回复我的消息
    @PostMapping("findAll")
    public R findAll(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                     @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                     HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询回复我的消息,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        List<ReplyMeVo> replyMeVos = replyMeService.findUserNews(current , limit , userId);
        Integer total = replyMeService.findUserNewsNumber(userId);
        replyMeService.setReplyMeRead(replyMeVos);
        return R.ok().data("total" , total).data("replyNewsList" , replyMeVos);
    }

    //删除用户回复消息
    @PostMapping("delete")
    public R delete(String id , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户删除回复消息,用户id:" + userId + ",消息id:" + id);
        if(userId == null || StringUtils.isEmpty(id)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        replyMeService.delete(id , userId);
        return R.ok();
    }

    //回复用户消息
    @PostMapping("addreply")
    public R addreply(String id , String content , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("回复用户消息,用户id:" + userId + ",消息id:" + id);
        if(userId == null || StringUtils.isEmpty(id) || StringUtils.isEmpty(content)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        replyMeService.addreply(id , content , userId);
        return R.ok();
    }
}

