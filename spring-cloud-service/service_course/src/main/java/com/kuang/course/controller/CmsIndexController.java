package com.kuang.course.controller;

import com.kuang.course.entity.vo.IndexCategoryVo;
import com.kuang.course.entity.vo.SlideTitleVo;
import com.kuang.course.service.CmsCourseService;
import com.kuang.course.service.CmsTwoCategoryService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/cms/index")
@Slf4j
public class CmsIndexController {

    @Resource
    private CmsTwoCategoryService twoCategoryService;

    @Resource
    private CmsCourseService courseService;

    //查询一级下面所有二级分类,和二级分类下面对应课程
    @GetMapping("findSecondLevelAndCourse")
    public R findSecondLevelAndCourse(String oneCategoryId){
        //校验数据
        if(StringUtils.isEmpty(oneCategoryId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        //查询侧边栏
        List<SlideTitleVo> slideTitleVoList = twoCategoryService.findSlideTitleByOcId(oneCategoryId);
        //查询相应的二级分类
        List<IndexCategoryVo> indexCategoryVoList = twoCategoryService.findIndexCategoryVoByOcId(oneCategoryId);
        return R.ok().data("slideTitleList" , slideTitleVoList).data("categoryList" , indexCategoryVoList);
    }
}
