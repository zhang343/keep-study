package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.ColunmArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.bbs.entity.vo.ColumnArticleVo;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-27
 */
public interface ColunmArticleMapper extends BaseMapper<ColunmArticle> {

    //查询专栏文章
    List<ColumnArticleVo> findColumnArticle(String columnId);
}
