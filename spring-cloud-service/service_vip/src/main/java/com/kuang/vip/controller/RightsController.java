package com.kuang.vip.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vip.entity.Rights;
import com.kuang.vip.service.RightsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-07
 * 权益处理类
 */
@RestController
@RequestMapping("/vm/trai")
@Slf4j
public class RightsController {

    @Resource
    private RightsService rightsService;

    //查询出vip权益,非普通会员权益
    @GetMapping("findVipRight")
    public R findVipRight(){
        log.info("查询出vip权益,非普通会员权益");
        List<Rights> rightsList = rightsService.findVipRight();
        return R.ok().data("vipList" , rightsList);
    }

    //查出用户对应课程打折数量
    @GetMapping("findUserCourseDiscount")
    public R findUserCourseDiscount(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查出成员对应课程打折数量,用户id：" + userId);
        if(userId == null){
            log.warn("用户尚未登录就查询课程打折多少");
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录再查看");
        }
        double courseDiscount = rightsService.findUserCourseDiscount(userId);
        return R.ok().data("courseDiscount" , courseDiscount);
    }
}

