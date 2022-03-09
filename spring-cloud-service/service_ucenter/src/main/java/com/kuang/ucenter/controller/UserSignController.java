package com.kuang.ucenter.controller;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;

@RestController
@RequestMapping("/user/sign")
@Slf4j
public class UserSignController {

    @Resource
    private UserInfoService userInfoService;

    //签到接口
    @PostMapping("toSignIn")
    public R toSignIn(HttpServletRequest request){
        System.out.println(LocalTime.now());
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户签到,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        userInfoService.toSignIn(userId);
        System.out.println(LocalTime.now());
        return R.ok();
    }
}
