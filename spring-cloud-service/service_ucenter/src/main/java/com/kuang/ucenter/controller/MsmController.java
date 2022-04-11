package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.service.MsmService;
import com.kuang.ucenter.utils.RandomUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/msm")
@CrossOrigin
public class MsmController {

    @Resource
    private MsmService msmService;

    //发送短信
    @GetMapping("send")
    public R sendMsm(String phone){
        if(StringUtils.isEmpty(phone)){
            throw new XiaoXiaException(ResultCode.ERROR, "电话号码不可为空");
        }
        String code = RandomUtil.getSixBitRandom();
        msmService.sendMsm(phone , code);
        return R.ok();
    }


}
