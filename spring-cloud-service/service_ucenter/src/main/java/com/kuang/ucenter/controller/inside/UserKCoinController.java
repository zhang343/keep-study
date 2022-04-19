package com.kuang.ucenter.controller.inside;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/KCoin")
@Slf4j
public class UserKCoinController {

    @Resource
    private UserInfoService userInfoService;

    //减少用户k币
    @PostMapping("reduce")
    public R reduce(Integer kCoinNumber , String userId){
        log.info("开始减少用户k币,减少数量:" + kCoinNumber);
        if(StringUtils.isEmpty(userId) || kCoinNumber == null || kCoinNumber == 0){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        //如果传递过来的是正值,修改为负值
        if(kCoinNumber > 0){
            kCoinNumber = -kCoinNumber;
        }
        log.info("给用户:" + userId + "减少" + kCoinNumber + "k币");
        userInfoService.addKCoin(kCoinNumber , userId);
        return R.ok();
    }

    //给用户增加k币
    @PostMapping("add")
    public R add(Integer kCoinNumber , String userId){
        log.info("开始增加用户k币,增加数量:" + kCoinNumber);
        if(StringUtils.isEmpty(userId) || kCoinNumber == null || kCoinNumber <= 0){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        userInfoService.addKCoin(kCoinNumber , userId);
        return R.ok();
    }

}
