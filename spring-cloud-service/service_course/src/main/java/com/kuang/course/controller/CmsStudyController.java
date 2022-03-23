package com.kuang.course.controller;


import com.kuang.course.entity.vo.CourseStudyVo;
import com.kuang.course.service.CmsStudyService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-23
 */
@RestController
@RequestMapping("/cms/study")
public class CmsStudyController {

    @Resource
    private CmsStudyService studyService;

    //查询用户学习记录
    @GetMapping("findUserStudy")
    public R findUserStudy(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                           @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                           String userId){
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确查询");
        }
        Integer total = studyService.findUserStudyNumber(userId);
        List<CourseStudyVo> courseStudyVoList = studyService.findUserStudy(userId , current , limit);
        return R.ok().data("total" , total).data("courseList" , courseStudyVoList);
    }
}

