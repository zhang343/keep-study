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

    //查询用户安全信息
    @GetMapping("findUserSecurityData")
    public R findUserSecurityData(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }

        UserSecurity userSecurity = userInfoService.findUserSecurityData(userId);
        return R.ok().data("userSecurity" , userSecurity);
    }

    //修改用户安全信息
    @PostMapping("setUserSecurityData")
    public R setUserSecurityData(HttpServletRequest request ,
                                 String email ,
                                 String password){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || (StringUtils.isEmpty(email) && StringUtils.isEmpty(password))){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        userInfoService.setUserSecurityData(userId , email , password);
        return R.ok();
    }


}
