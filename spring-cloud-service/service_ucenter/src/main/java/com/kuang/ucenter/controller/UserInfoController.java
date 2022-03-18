package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.client.BbsClient;
import com.kuang.ucenter.entity.UserInfo;
import com.kuang.ucenter.entity.vo.HomePageVo;
import com.kuang.ucenter.entity.vo.MyUserInfoVo;
import com.kuang.ucenter.entity.vo.UserDetailVo;
import com.kuang.ucenter.entity.vo.UserSetDataVo;
import com.kuang.ucenter.service.UserAttentionService;
import com.kuang.ucenter.service.UserInfoService;
import com.kuang.ucenter.service.UserStudyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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




}

