package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.ColunmArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-27
 */
public interface ColunmArticleMapper extends BaseMapper<ColunmArticle> {

    //查询专栏文章
    List<ColumnArticleVo> findColumnArticle(String columnId);

    //查询专栏文章idList
    List<String> findArticleIdListByColunmId(String columnId);
}
