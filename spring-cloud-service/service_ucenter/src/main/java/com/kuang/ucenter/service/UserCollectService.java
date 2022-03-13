package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.ucenter.entity.vo.CollectArticleVo;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserCollectService extends IService<UserCollect> {

    //查询用户是否收藏了某个文章
    boolean findUserIsCollection(String articleId, String userId);

    //用户收藏文章
    void addCollectionArticle(String articleId, String userId);

    //查询我的收藏文章数量
    Integer findUserCollectionNumber(String userId);

    //查询用户收藏
    Future<List<CollectArticleVo>> findUserCollection(Long current, Long limit, String userId);
}
