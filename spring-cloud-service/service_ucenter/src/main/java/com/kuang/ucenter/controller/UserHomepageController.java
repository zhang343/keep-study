package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.UserHomepage;
import com.kuang.ucenter.entity.vo.HomePageVo;
import com.kuang.ucenter.service.UserHomepageService;
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
@RequestMapping("/user/homepage")
@Slf4j
public class UserHomepageController {

    @Resource
    private UserHomepageService userHomepageService;


    //修改用户主页内容
    @PostMapping("update")
    public R update(String content , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("修改用户主页内容");
        if(userId == null || StringUtils.isEmpty(content)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        userHomepageService.updateContent(content  , userId);
        return R.ok();
    }

    //查看主页内容
    @GetMapping("findByUserId")
    public R findByUserId(String userId){
        log.info("查看用户主页内容");
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确查询");
        }
        UserHomepage userHomepage = userHomepageService.findByUserId(userId);
        return R.ok().data("content" , userHomepage.getContent());
    }

}

