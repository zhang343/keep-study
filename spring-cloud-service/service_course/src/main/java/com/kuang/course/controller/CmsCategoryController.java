package com.kuang.course.controller;


import com.kuang.course.entity.vo.OneCategoryVo;
import com.kuang.course.service.CmsOneCategoryService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/cms/category")
@Slf4j
public class CmsCategoryController {

    @Resource
    private CmsOneCategoryService cmsOneCategoryService;

    //查询所有一级分类
    @GetMapping("findAllFirstLevel")
    public R findAllFirstLevel(){
        List<OneCategoryVo> oneCategoryList = cmsOneCategoryService.findAllFirstLevel();
        return R.ok().data("categoryFirstList" , oneCategoryList);
    }
}
