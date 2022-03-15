package com.kuang.bbs.service;

import com.kuang.bbs.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.vo.CategoryVo;

import java.util.List;
import java.util.TreeMap;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface CategoryService extends IService<Category> {

    //查询所有分类
    List<Category> findAllCategory();

    //查询出所有分类
    List<CategoryVo> findAllCategoryVo();


    TreeMap<String , Category> findAllCategoryTreeMap();

    //查询指定的分类
    Category findCategoryByCategoryId(String categoryId);
}
