package com.kuang.bbs.service.impl;

import com.kuang.bbs.entity.vo.ArticleCacheVo;
import com.kuang.bbs.service.CacheArticleService;
import com.kuang.springcloud.utils.RedisUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CacheArticleServiceImpl implements CacheArticleService {

    //文章缓存,时间是30分钟
    @Override
    public void setArticleCache(ArticleCacheVo articleCacheVo, String userId) {
        List<String> labelList = articleCacheVo.getLabelList();
        if(labelList != null && labelList.size() != 0){
            Set<String> labelSet = new HashSet<>(labelList);
            labelList = new ArrayList<>(labelSet);
            articleCacheVo.setLabelList(labelList);
        }
        RedisUtils.setValueTimeout(userId , articleCacheVo , 30 * 60);
    }

    //查询文章缓存
    @Override
    public ArticleCacheVo findArticleCache(String userId) {
        Object value = RedisUtils.getValue(userId);
        if(value == null){
            return null;
        }
        return (ArticleCacheVo) value;
    }
}
