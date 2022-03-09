package com.kuang.vip.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vip.service.UserTodayRightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Xiaozhang
 * @since 2022-02-27
 */
@RestController
@RequestMapping("/usertodayright")
@Slf4j
public class UserTodayRightController {


    @Resource
    private UserTodayRightService userTodayRightService;

    //用户每日文章权益
    @PostMapping("addArticle")
    public R addArticle(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户发布文章,每日文章权益减1,用户id:" + userId);
        if(userId == null){
            log.warn("有人试图非法操作,减去用户每日文章权益,用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        userTodayRightService.addArticle(userId);
        return R.ok();
    }


    //签到接口权益
    @PostMapping("toSign")
    public R toSign(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户签到接口权益用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        userTodayRightService.toSign(userId);
        return R.ok();
    }

}

