package com.kuang.bbs.service;

import com.kuang.bbs.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface CategoryService extends IService<Category> {

    //查询所有分类
    List<Category> findAllCategory();

    //查询指定的分类
    Category findCategoryByCategoryId(String categoryId);
}
