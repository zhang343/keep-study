package com.kuang.ucenter.controller;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.vo.OtherUserDetailVo;
import com.kuang.ucenter.service.UserAttentionService;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/other")
@Slf4j
public class OtherUserController {

    @Resource
    private UserAttentionService attentionService;

    @Resource
    private UserInfoService userInfoService;

    //查询其他用户主页内容
    @GetMapping("findUserHomePage")
    public R findUserHomePage(String userId , HttpServletRequest request){
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请传入正确参数");
        }
        OtherUserDetailVo otherUserDetailVo = userInfoService.findOtherUserHomePage(userId);
        boolean isAttention = false;
        String myuserId = JwtUtils.getMemberIdByJwtToken(request);
        if(myuserId != null){
            isAttention = attentionService.findAIsAttentionB(myuserId , userId);
        }
        return R.ok().data("isAttention" , isAttention).data("otherUserDetail" , otherUserDetailVo);
    }
}
