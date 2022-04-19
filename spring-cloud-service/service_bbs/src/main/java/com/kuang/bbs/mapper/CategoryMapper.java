package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.bbs.entity.vo.CategoryVo;

import java.util.List;


public interface CategoryMapper extends BaseMapper<Category> {

    //查询所有分类
    List<CategoryVo> findAllCategoryVo();
}
