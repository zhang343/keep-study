package com.kuang.ucenter.controller;


import com.kuang.springcloud.utils.R;
import com.kuang.ucenter.service.UserHeadPortraitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-07
 */
@RestController
@RequestMapping("/user/avatar")
@Slf4j
public class UserHeadPortraitController {

    @Resource
    private UserHeadPortraitService userHeadPortraitService;

    //查询出所有头像
    @GetMapping("findAllAvatar")
    public R findAllAvatar(){
        List<String> urlList = userHeadPortraitService.findAllAvatar();
        return R.ok().data("avatarList" , urlList);
    }


}

