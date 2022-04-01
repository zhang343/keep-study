package com.kuang.download.controller;


import com.kuang.download.entity.vo.DtmCategoryVo;
import com.kuang.download.service.DtmCategoryService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/dtm/category")
@Slf4j
public class DtmCategoryController {

    @Resource
    private DtmCategoryService dtmCategoryService;

    //查出所有分类
    @GetMapping("findAllCategory")
    public R findAllCategory(){
        List<DtmCategoryVo> dtmCategoryList = dtmCategoryService.findAll();
        return R.ok().data("categoryList" , dtmCategoryList);
    }
}

