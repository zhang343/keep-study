package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.ArticleRight;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author Xiaozhang
 * @since 2022-03-21
 */
public interface ArticleRightMapper extends BaseMapper<ArticleRight> {

    //更新每日文章权益
    Integer updateUserArticleRight();
}
