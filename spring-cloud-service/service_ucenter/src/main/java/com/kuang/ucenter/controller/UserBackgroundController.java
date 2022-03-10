package com.kuang.ucenter.controller;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.service.UserBackgroundService;
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
 * @author Xiaozhang
 * @since 2022-02-05
 */
@RestController
@RequestMapping("/user/bgimg")
@Slf4j
public class UserBackgroundController {


    @Resource
    private UserBackgroundService backgroundService;

    @Resource
    private UserInfoService userInfoService;

    //查看所有背景图像
    @GetMapping("findAll")
    public R findAll(){
        log.info("查看所有背景图像");
        List<String> urlList = backgroundService.findAll();
        return R.ok().data("bgImgList" , urlList);
    }

    //修改用户背景图像
    @PostMapping("setBgImg")
    public R setBgImg(String bgImg , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("修改用户背景图像,用户id:" + userId + ",背景图片地址:" + bgImg);
        if(userId == null || StringUtils.isEmpty(bgImg)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        userInfoService.setBgImg(bgImg , userId);
        return R.ok();
    }
}
