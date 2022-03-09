package com.kuang.ucenter.controller;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.vo.UserSecurity;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/security")
@Slf4j
public class UserSecurityController {

    @Resource
    private UserInfoService userInfoService;

    //修改用户邮箱和密码
    @PostMapping("setEP")
    public R setEP(String email , String password , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("设置用户邮箱和密码,用户id:" + userId + ",邮箱:" + email + "密码:" + password);
        if(userId == null || StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        userInfoService.setEmailAndPassword(userId , email , password);
        return R.ok();
    }

    //查询用户安全信息
    @GetMapping("findAWEP")
    public R findAWEP(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询用户安全信息,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        UserSecurity userSecurity = userInfoService.findAWEP(userId);
        return R.ok().data("userSecurity" , userSecurity);
    }

}
