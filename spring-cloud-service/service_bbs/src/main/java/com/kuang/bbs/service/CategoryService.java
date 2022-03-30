package com.kuang.bbs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.Category;
import com.kuang.bbs.entity.vo.CategoryVo;

import java.util.List;

public interface CategoryService extends IService<Category> {

    //查询出所有分类
    List<CategoryVo> findAllCategoryVo();

    //查询指定的分类
    Category findCategoryByCategoryId(String categoryId);
}
