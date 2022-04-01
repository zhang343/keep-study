package com.kuang.course.service;

import com.kuang.course.entity.CmsOneCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.course.entity.vo.OneCategoryVo;

import java.util.List;


public interface CmsOneCategoryService extends IService<CmsOneCategory> {

    //查询所有一级分类
    List<OneCategoryVo> findAllFirstLevel();
}
