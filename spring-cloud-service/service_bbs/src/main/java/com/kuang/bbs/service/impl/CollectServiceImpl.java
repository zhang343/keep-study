package com.kuang.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.bbs.entity.Collect;
import com.kuang.bbs.entity.vo.CollectArticleVo;
import com.kuang.bbs.mapper.CollectMapper;
import com.kuang.bbs.service.CollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.ResultCode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-23
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    //查看用户是否收藏了该文章
    @Override
    public boolean findUserIsCollection(String articleId, String userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("article_id" , articleId);
        return baseMapper.selectCount(wrapper) != 0;
    }

    //增加用户收藏文章
    @Override
    public void addUserCollectArticle(String userId, String articleId) {
        boolean userIsCollection = findUserIsCollection(articleId, userId);
        if(userIsCollection){
            throw new XiaoXiaException(ResultCode.ERROR , "你已经收藏了该文章");
        }
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setArticleId(articleId);
        int insert = baseMapper.insert(collect);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "增加收藏失败");
        }
    }

    //删除用户收藏文章
    @Override
    public void deleteUserCollectArticle(String userId, String articleId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("article_id" , articleId);
        int delete = baseMapper.delete(wrapper);
        if(delete != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "删除收藏失败");
        }
    }

    //查询用户收藏文章
    @Override
    public Integer findUserCollectArticleNumber(String userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查询用户收藏文章
    @Override
    public List<CollectArticleVo> findUserCollectArticle(String userId, Long current, Long limit) {
        current = (current - 1) * limit;
        return baseMapper.findUserCollectArticle(userId , current , limit);
    }
}
