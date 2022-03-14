package com.kuang.course.mapper;

import com.kuang.course.entity.CmsOneCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.course.entity.vo.OneCategoryVo;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
public interface CmsOneCategoryMapper extends BaseMapper<CmsOneCategory> {

    //查询所有一级分类
    List<OneCategoryVo> findAllFirstLevel();
}
