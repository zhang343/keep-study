package com.kuang.vip.controller.Inside;

import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vip.service.RightsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inside/right")
@Slf4j
public class InsideRightController {

    @Resource
    private RightsService rightsService;


    //查询用户权益
    @GetMapping("findRightRedisByUserId")
    public R findRightRedisByUserId(String userId){
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确查找");
        }
        RightRedis rightByUserId = rightsService.findRightByUserId(userId);
        return R.ok().data("rightRedis" , rightByUserId);
    }
}
