package com.kuang.bbs.controller;


import com.kuang.bbs.entity.vo.CategoryVo;
import com.kuang.bbs.service.CategoryService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/bbs/category")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    //查询所有分类
    @GetMapping("findAllCategory")
    public R findAllCategory(){
        List<CategoryVo> categoryList = categoryService.findAllCategoryVo();
        return R.ok().data("categoryList" , categoryList);
    }

}

