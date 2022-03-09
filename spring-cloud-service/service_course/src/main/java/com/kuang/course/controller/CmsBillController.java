package com.kuang.course.controller;


import com.kuang.course.service.CmsBillService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
@RestController
@RequestMapping("/cms/bill")
@Slf4j
public class CmsBillController {

    @Resource
    private CmsBillService billService;

    //查询用户购买课程数量
    @GetMapping("findUserBillNumber")
    public R findUserBillNumber(String userId){
        log.info("查询用户购买课程数量,用户id:" + userId);
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Integer buyCourseNumber = billService.findUserBillNumber(userId);
        return R.ok().data("buyCourseNumber" , buyCourseNumber);
    }

}

