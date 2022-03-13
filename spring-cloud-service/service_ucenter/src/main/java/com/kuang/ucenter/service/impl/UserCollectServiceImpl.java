package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.ucenter.entity.UserCollect;
import com.kuang.ucenter.entity.vo.CollectArticleVo;
import com.kuang.ucenter.mapper.UserCollectMapper;
import com.kuang.ucenter.service.UserCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Service
@Slf4j
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements UserCollectService {

    //查询用户是否收藏了某个文章
    @Override
    public boolean findUserIsCollection(String articleId, String userId) {
        log.info("查询用户是否收藏了文章,文章id:" + articleId + ",用户id:" + userId);
        QueryWrapper<UserCollect> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        wrapper.eq("user_id" , userId);
        Integer integer = baseMapper.selectCount(wrapper);
        return integer != 0;
    }

    //用户收藏文章
    @Async
    @Override
    public void addCollectionArticle(String articleId, String userId) {
        log.info("用户收藏文章,文章id:" + articleId + ",用户id:" + userId);
        boolean flag = findUserIsCollection(articleId, userId);
        if(!flag){
            UserCollect userCollect = new UserCollect();
            userCollect.setArticleId(articleId);
            userCollect.setUserId(userId);
            baseMapper.insert(userCollect);
        }
    }

    //查询我的收藏文章数量
    @Override
    public Integer findUserCollectionNumber(String userId) {
        log.info("用户收藏文章数量,用户id:" + userId);
        QueryWrapper<UserCollect> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查询用户收藏
    @Async
    @Override
    public Future<List<CollectArticleVo>> findUserCollection(Long current, Long limit, String userId) {
        current = (current - 1) * limit;
        return new AsyncResult<>(null);
    }
}
