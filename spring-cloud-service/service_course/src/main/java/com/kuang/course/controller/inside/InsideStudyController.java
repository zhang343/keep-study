package com.kuang.course.controller.inside;

import com.kuang.course.service.CmsStudyService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inside/study")
@Slf4j
public class InsideStudyController {

    @Resource
    private CmsStudyService studyService;

    //查询用户学习数量
    @GetMapping("findUserStudyNumber")
    public R findUserStudyNumber(String userId){
        Integer studyNumber = studyService.findUserStudyNumber(userId);
        return R.ok().data("studyNumber" , studyNumber);
    }
}
