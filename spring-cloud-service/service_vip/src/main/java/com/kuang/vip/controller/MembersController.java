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

/**
 * @author Xiaozhang
 * @since 2022-02-07
 * vip用户处理类
 */
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
        log.info("用户充值vip,充值的vip的id:" + id);
        if(StringUtils.isEmpty(id) || userId == null){
            log.warn("有用户非法充值vip操作,vip的id：" + id + ",用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        membersService.addMember(id , userId);
        return R.ok();
    }


}

