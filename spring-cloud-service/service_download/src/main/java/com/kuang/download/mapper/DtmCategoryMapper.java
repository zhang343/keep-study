package com.kuang.download.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.download.entity.DtmCategory;
import com.kuang.download.entity.vo.DtmCategoryVo;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface DtmCategoryMapper extends BaseMapper<DtmCategory> {

    //查询出所有分类
    List<DtmCategoryVo> findAll();
}
