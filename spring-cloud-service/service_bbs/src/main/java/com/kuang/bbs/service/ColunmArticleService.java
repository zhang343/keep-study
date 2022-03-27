package com.kuang.bbs.service;

import com.kuang.bbs.entity.ColunmArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.vo.ColumnArticleVo;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-27
 */
public interface ColunmArticleService extends IService<ColunmArticle> {

    //查询专栏文章
    List<ColumnArticleVo> findColumnArticle(String columnId);
}
