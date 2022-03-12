package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserCollect;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
