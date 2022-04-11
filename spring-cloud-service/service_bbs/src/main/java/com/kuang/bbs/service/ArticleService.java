package com.kuang.bbs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.vo.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;


public interface ArticleService extends IService<Article> {

    //查询系统文章数量
    Integer findArticleNumber();

    //查询文章详细数据
    ArticleVo findArticleDetail(String articleId , String userId);

    //根据条件查询系统文章总数
    Long findArticleNumber(String categoryId, Boolean isExcellentArticle, String articleNameOrLabelName);

    //条件分页查询文章
    Future<List<IndexArticleVo>> pageArticleCondition(Long current, Long limit, String categoryId, Boolean isExcellentArticle, String articleNameOrLabelName);

    //用户发布文章
    Article addArticle(ArticleUpdateAndCreateVo articleUpdateAndCreateVo, String avatar , String nickname , String userId);

    //查询江湖文章以便修改
    ArticleUpdateAndCreateVo findArticleByArticleIdAndUserId(String articleId, String userId);

    //用户修改江湖文章
    void updateArticle(ArticleUpdateAndCreateVo articleUpdateAndCreateVo, String userId);

    //用户删除文章
    void deleteArticle(String articleId, String userId);

    //查询置顶文章
    IndexArticleVo findTopArticle();

    //向好友动态发送消息
    void sendFrientFeed(Article article , String userId);

    //设置文章访问量，缓存处理
    void setArticleViews(String articleId , String ip);

    //为消息模块服务，查询文章浏览量
    Map<String, Object> findArticleViews(List<String> articleIdList);

    //为用户模块服务，查询用户文章在江湖可以查找到的文章
    Integer findUserbbsArticleNumber(String userId);

    //查询出文章浏览量
    List<Article> findArticleViewsList(List<String> articleIdList);

    //更新文章浏览量
    void updateArticleViews(List<Article> articleUpdateList);

    //查询用户所有江湖文章数量
    Integer findUserAllArticleNumber(String userId);

    //查找用户在江湖的所有文章
    List<UserArticleVo> findUserAllArticle(String userId, Long current, Long limit);

    //查询他人在江湖发布的文章
    List<OtherUserArticleVo> findOtherUserArticle(String userId, Long current, Long limit);

    //修改专栏文章的排序
    void updateArticleSort(String articleId, String userId, Long sort);

    //查询专栏文章
    ColumnArticleDetailVo findColunmArticle(String articleId);

    //设置置顶文章
    void setTopArticle(String articleId);

    //封禁该文章
    void banArticle(String reportId, String articleId);
}
