package com.kuang.bbs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.vo.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface ArticleService extends IService<Article> {

    //查询系统文章数量
    Integer findArticleNumber();

    //查询文章详细数据
    Future<ArticleVo> findArticleDetail(String articleId , String userId);

    //文章缓存,时间是30分钟
    void setArticleCache(ArticleCacheVo articleCacheVo, List<String> labelList, String userId);

    //查询文章缓存
    ArticleCacheVo findArticleCache(String userId);

    //根据条件查询系统文章总数
    Long findArticleNumber(String categoryId, Boolean isExcellentArticle, String articleNameOrLabelName);

    //条件分页查询文章
    Future<List<IndexArticleVo>> pageArticleCondition(Long current, Long limit, String categoryId, Boolean isExcellentArticle, String articleNameOrLabelName);

    //用户发布文章
    Article addArticle(ArticleUpdateAndCreateVo articleUpdateAndCreateVo, String userId);

    //查询文章通过文章id和用户id
    ArticleUpdateAndCreateVo findArticleByArticleIdAndUserId(String articleId, String userId);

    //用户修改文章
    Article updateArticle(ArticleUpdateAndCreateVo articleUpdateAndCreateVo, String userId);

    //用户删除文章
    void deleteArticle(String articleId, String userId);

    //查询置顶文章
    IndexArticleVo findTopArticle();

    //查找文章所有者和文章标题
    Article findArticleUserIdAndTitle(String articleId);

    //查找用户所有文章数量
    Integer findArticleNumberByUserId(String userId);

    //查询用户已经发布和没有违规的文章数量
    Integer findReleaseArticleNumber(String userId);

    //向好友动态发送消息
    void sendFrientFeed(String articleId , String token);

    //查找文章浏览量
    Map<String, Object> findArticleViews(List<String> articleIdList);

    //查询用户收藏文章数量
    Future<Integer> findUserCollectionNumber(String token);

    //查找用户我的文章
    List<UserArticleVo> findMyArticle(Long current, Long limit, String userId);

    //查询是不是专栏文章
    boolean findIsColumnArticle(String articleId);
}
