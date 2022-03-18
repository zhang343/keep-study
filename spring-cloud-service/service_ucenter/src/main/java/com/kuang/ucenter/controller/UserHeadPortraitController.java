package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.service.UserHeadPortraitService;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xiaozhang
 * @since 2022-03-07
 */
@RestController
@RequestMapping("/user/headportrait")
@Slf4j
public class UserHeadPortraitController {

    @Resource
    private UserHeadPortraitService userHeadPortraitService;

    //查询出所有头像
    @GetMapping("findAll")
    public R findAll(){
        log.info("查询出所有头像");
        List<String> urlList = userHeadPortraitService.findAll();
        return R.ok().data("avatarList" , urlList);
    }


}

