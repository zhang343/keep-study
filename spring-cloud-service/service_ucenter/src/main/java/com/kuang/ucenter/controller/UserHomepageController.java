package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.service.UserHomepageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/user/introduce")
@Slf4j
public class UserHomepageController {

    @Resource
    private UserHomepageService userHomepageService;

    //查询用户简介
    @GetMapping("findUserIntroduce")
    public R findUserIntroduce(String userId){
        String content = userHomepageService.findUserIntroduce(userId);
        return R.ok().data("content" , content);
    }

    //修改用户主页内容
    @PostMapping("setUserIntroduce")
    public R setUserIntroduce(HttpServletRequest request , String content){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(content)){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        userHomepageService.setUserIntroduce(userId , content);
        return R.ok();
    }

}

