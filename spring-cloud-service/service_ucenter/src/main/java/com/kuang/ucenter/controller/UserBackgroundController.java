package com.kuang.ucenter.controller;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.service.UserBackgroundService;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("findAllBgimg")
    public R findAllBgimg(){
        List<String> urlList = backgroundService.findAllBgimg();
        return R.ok().data("bgImgList" , urlList);
    }

    //修改用户背景图像
    @PostMapping("setUserBgimg")
    public R setUserBgimg(HttpServletRequest request , String url){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(url)){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        userInfoService.setUserBgimg(userId , url);
        return R.ok();
    }

}
