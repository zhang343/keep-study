package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.ColumnAuthor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.bbs.entity.vo.ColumnAuthorVo;

import java.util.List;


public interface ColumnAuthorMapper extends BaseMapper<ColumnAuthor> {

    //查询专栏作者
    List<ColumnAuthorVo> findColumnAuthorList(String columnId);
}
