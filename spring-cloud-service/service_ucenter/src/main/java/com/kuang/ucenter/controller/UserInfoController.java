package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.vo.MyUserInfoVo;
import com.kuang.ucenter.entity.vo.UserDetailVo;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@RestController
@RequestMapping("/user/account")
@Slf4j
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;


    //用户账号密码登录
    @PostMapping("login")
    public R login(String loginAct , String loginPwd){
        log.info("用户登录,账号:" + loginAct + ",密码:" + loginPwd);
        if(StringUtils.isEmpty(loginAct) || StringUtils.isEmpty(loginPwd)){
            throw new XiaoXiaException(ResultCode.ERROR , "登录失败");
        }
        String userId = userInfoService.login(loginAct , loginPwd);
        String token = JwtUtils.getJwtToken(userId);
        return R.ok().data("token" , token);
    }


    //用户登录之后查询小方框内容
    @GetMapping("findUserSmallBoxContent")
    public R findUserSmallBoxContent(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        MyUserInfoVo myUserInfoVo = userInfoService.findUserSmallBoxContent(userId);
        return R.ok().data("myUserInfoVo" , myUserInfoVo);
    }


    //用户签到
    @PostMapping("userSignIn")
    public R userSignIn(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        int signExperience = userInfoService.userSignIn(userId);
        return R.ok().data("signExperience" , signExperience);
    }


    //查询用户上边框的内容
    @GetMapping("findUserBorderTop")
    public R findUserBorderTop(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        UserDetailVo userDetailVo = userInfoService.findUserBorderTop(userId);
        return R.ok().data("UserDetail" , userDetailVo);
    }

}

