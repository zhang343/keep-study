package com.kuang.vip.controller.Inside;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vip.service.UserTodayRightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inside/dailyequity")
@Slf4j
public class InsideUserTodayRightController {

    @Resource
    private UserTodayRightService userTodayRightService;


    //查询用户是否签到
    @GetMapping("findUserIsSign")
    public R findUserIsSign(String userId){
        log.info("查询用户是否签到,用户id:" + userId);
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Boolean isSign = userTodayRightService.findIsSign(userId);
        return R.ok().data("isSign" , isSign);
    }

    //用户每日文章权益
    @PostMapping("addArticle")
    public R addArticle(String userId){
        log.info("用户发布文章,每日文章权益减1,用户id:" + userId);
        if(StringUtils.isEmpty(userId)){
            log.warn("有人试图非法操作,减去用户每日文章权益,用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        userTodayRightService.addArticle(userId);
        return R.ok();
    }

    //签到接口权益
    @PostMapping("toSign")
    public R toSign(String userId){
        log.info("用户签到接口权益用户id:" + userId);
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        userTodayRightService.toSign(userId);
        return R.ok();
    }
}
