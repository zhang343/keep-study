package com.kuang.bbs.service;

import com.kuang.bbs.entity.vo.ArticleCacheVo;

public interface CacheArticleService {

    //文章缓存,时间是30分钟
    void setArticleCache(ArticleCacheVo articleCacheVo, String userId);

    //查询文章缓存
    ArticleCacheVo findArticleCache(String userId);
}
