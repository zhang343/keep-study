package com.kuang.bbs.service;

import com.kuang.bbs.entity.Collect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.vo.CollectArticleVo;

import java.util.List;

public interface CollectService extends IService<Collect> {

    //查看用户是否收藏了该文章
    boolean findUserIsCollection(String articleId, String userId);

    //增加用户收藏文章
    void addUserCollectArticle(String userId, String articleId);

    //删除用户收藏文章
    void deleteUserCollectArticle(String userId, String articleId);

    //查询用户收藏文章数量
    Integer findUserCollectArticleNumber(String userId);

    //查询用户收藏文章
    List<CollectArticleVo> findUserCollectArticle(String userId, Long current, Long limit);
}
