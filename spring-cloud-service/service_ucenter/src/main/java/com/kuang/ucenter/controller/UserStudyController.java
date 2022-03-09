package com.kuang.ucenter.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.client.CourseClient;
import com.kuang.ucenter.entity.UserStudy;
import com.kuang.ucenter.service.UserStudyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@RestController
@RequestMapping("/user/historical")
@Slf4j
public class UserStudyController {

    @Resource
    private UserStudyService userStudyService;

    @Resource
    private CourseClient courseClient;

    @GetMapping("findAll")
    public R findAll(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                     @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                     String userId){
        log.info("查询用户历史记录,用户id:" + userId);
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }

        Future<Page<UserStudy>> pageStudy = userStudyService.findUserStudy(userId , current , limit);
        R userBillR = courseClient.findUserBillNumber(userId);
        long total = 0L;
        List<UserStudy> userStudyList = null;
        try {
            Page<UserStudy> page = pageStudy.get();
            total = page.getTotal();
            userStudyList = page.getRecords();
        }catch(Exception e){
            log.error("查询用户历史记录失败");
            throw new XiaoXiaException(ResultCode.ERROR , "查询失败");
        }

        return R.ok().data("buyCourseNumber" , userBillR.getData().get("buyCourseNumber"))
                .data("total" , total)
                .data("courseList" , userStudyList);

    }


}

