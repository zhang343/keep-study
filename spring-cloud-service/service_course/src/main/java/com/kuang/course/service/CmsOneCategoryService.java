package com.kuang.course.service;

import com.kuang.course.entity.CmsOneCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
public interface CmsOneCategoryService extends IService<CmsOneCategory> {

    //查询所有一级分类
    List<CmsOneCategory> findAll();
}
