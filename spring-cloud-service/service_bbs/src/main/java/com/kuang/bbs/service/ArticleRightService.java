package com.kuang.bbs.service;

import com.kuang.bbs.entity.ArticleRight;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ArticleRightService extends IService<ArticleRight> {

    //修改用户当日文章权益
    void updateArticleRight(String userId);

    //插入文章权益，用户模块创建用户会用到
    void addArticleRight(String userId);
}
