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
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 * 首页处理类
 */
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
    public R findSecondLevelAndCourse(String id){
        log.info("查询一级下面所有二级分类,和二级分类下面对应课程,一级分类id:" + id);
        if(StringUtils.isEmpty(id)){
            log.warn("有人试图非法操作查询");
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        List<SlideTitleVo> slideTitleVoList = twoCategoryService.findSlideTitleByOcId(id);
        List<IndexCategoryVo> indexCategoryVoList = twoCategoryService.findIndexCategoryVoByOcId(id);
        for(IndexCategoryVo indexCategoryVo : indexCategoryVoList){
            indexCategoryVo.setCourseList(courseService.findCourseByTcId(indexCategoryVo.getId()));
        }
        return R.ok().data("slideTitleList" , slideTitleVoList).data("categoryList" , indexCategoryVoList);
    }
}
