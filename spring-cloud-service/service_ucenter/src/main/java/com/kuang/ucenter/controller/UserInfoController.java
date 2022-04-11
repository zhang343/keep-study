package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.UserInfo;
import com.kuang.ucenter.entity.vo.*;
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


    //注册
    @PostMapping("register")
    public R register(String phoneNumber , String code , String nickName){
        if(StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickName)){
            throw new XiaoXiaException(ResultCode.ERROR , "信息不完善");
        }

        UserInfo phoneNumberMember = userInfoService.getPhoneNumberMember(phoneNumber);
        if(phoneNumberMember != null){
            throw new XiaoXiaException(ResultCode.ERROR , "该账号已经注册");
        }

        userInfoService.insertMember(phoneNumber , nickName , code);
        return R.ok();
    }

    //验证码登录
    @PostMapping("loginCode")
    public R loginCode(String phoneNumber , String code){
        if(StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(code)){
            throw new XiaoXiaException(ResultCode.ERROR , "信息不完善");
        }
        String userId = userInfoService.loginCode(phoneNumber , code);
        String token = JwtUtils.getJwtToken(userId);
        return R.ok().data("token" , token);
    }

    //用户账号密码登录
    @PostMapping("login")
    public R login(String loginAct , String loginPwd){
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


    //查询用户主页的内容,这里查自己
    @GetMapping("findUserHomePage")
    public R findUserHomePage(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        UserDetailVo userDetailVo = userInfoService.findUserHomePage(userId);
        return R.ok().data("userDetail" , userDetailVo);
    }

    //查询右下边框内容
    @GetMapping("findLowerRightBox")
    public R findLowerRightBox(String userId){
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }

        UserLowerRightBox userLowerRightBox = userInfoService.findLowerRightBox(userId);
        return R.ok().data("userLowerRightBox" , userLowerRightBox);
    }


    //查询出用户资料
    @GetMapping("findUserData")
    public R findUserData(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }

        UserDateVo userDateVo = userInfoService.findUserData(userId);
        return R.ok().data("userDate" , userDateVo);

    }


    //修改用户资料
    @PostMapping("setUserData")
    public R setUserData(HttpServletRequest request , UserSetDataVo userSetDataVo){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        userInfoService.setUserData(userId , userSetDataVo);
        return R.ok();
    }

}

