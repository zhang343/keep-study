package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.bbs.entity.vo.ArticleVo;
import com.kuang.bbs.entity.vo.IndexArticleVo;
import com.kuang.bbs.entity.vo.UserArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface ArticleMapper extends BaseMapper<Article> {

    //根据条件查询系统文章总数
    Long findArticleNumber(@Param("categoryId") String categoryId ,
                           @Param("isExcellentArticle") Boolean isExcellentArticle ,
                           @Param("articleNameOrLabelName") String articleNameOrLabelName);

    //查找置顶文章
    IndexArticleVo findTopArticle();

    //条件分页查询文章
    List<IndexArticleVo> pageArticleCondition(@Param("current") Long current,
                                              @Param("limit") Long limit,
                                              @Param("categoryId") String categoryId,
                                              @Param("isExcellentArticle") Boolean isExcellentArticle,
                                              @Param("articleNameOrLabelName") String articleNameOrLabelName);


    //更新文章浏览量
    Long updateArticleViews(@Param("articleList") List<Article> articleList);
}
