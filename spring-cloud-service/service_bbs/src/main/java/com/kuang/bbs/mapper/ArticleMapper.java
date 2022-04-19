package com.kuang.bbs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.vo.IndexArticleVo;
import com.kuang.bbs.entity.vo.OtherUserArticleVo;
import com.kuang.bbs.es.entity.EsArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ArticleMapper extends BaseMapper<Article> {

    //条件分页查询文章
    List<IndexArticleVo> pageArticleCondition(@Param("current") Long current,
                                              @Param("limit") Long limit);

    //更新文章浏览量
    void updateArticleViews(@Param("articleUpdateList") List<Article> articleUpdateList);

    //查询他人在江湖发布的文章
    List<OtherUserArticleVo> findOtherUserArticle(@Param("userId") String userId,
                                                  @Param("current") Long current,
                                                  @Param("limit") Long limit);


    List<IndexArticleVo> getIndexArticleList(@Param("esArticleList") List<EsArticle> esArticleList);
}
