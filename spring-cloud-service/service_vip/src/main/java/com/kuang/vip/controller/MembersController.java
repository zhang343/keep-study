package com.kuang.vip.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vip.service.MembersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/vm/user")
@Slf4j
public class MembersController {

    @Resource
    private MembersService membersService;


    //用户充值vip
    @PostMapping("addMember")
    public R addMember(String id , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(id) || userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        membersService.addMember(id , userId);
        return R.ok();
    }

}

