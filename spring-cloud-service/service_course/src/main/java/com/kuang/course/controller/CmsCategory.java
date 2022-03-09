package com.kuang.course.controller;


import com.kuang.course.entity.CmsOneCategory;
import com.kuang.course.service.CmsOneCategoryService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 * 分类处理类
 */
@RestController
@RequestMapping("/cms/category")
@Slf4j
public class CmsCategory {

    @Resource
    private CmsOneCategoryService cmsOneCategoryService;

    //查询所有一级分类
    @GetMapping("findAllFirstLevel")
    public R findAllFirstLevel(){
        log.info("查询所有一级分类");
        List<CmsOneCategory> oneCategoryList = cmsOneCategoryService.findAll();
        return R.ok().data("categoryFirstList" , oneCategoryList);
    }
}
