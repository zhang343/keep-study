package com.kuang.bbs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.Comment;
import com.kuang.bbs.entity.vo.OneCommentVo;
import com.kuang.bbs.entity.vo.TwoCommentVo;
import com.kuang.bbs.mapper.ArticleMapper;
import com.kuang.bbs.mapper.CommentMapper;
import com.kuang.bbs.service.CommentService;
import com.kuang.springcloud.entity.InfoReplyMeVo;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.rabbitmq.MsgProducer;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;

@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private MsgProducer msgProducer;

    //查询系统评论数量
    @Cacheable(value = "commentNumber")
    @Override
    public Integer findCommentNumber() {
        return baseMapper.selectCount(null);
    }

    //用户删除评论
    @Override
    public void deleteComment(String commentId, String userId) {
        //删除指定评论
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("id" , commentId);
        wrapper.eq("user_id" , userId);
        int delete = baseMapper.delete(wrapper);
        if(delete != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "删除评论失败");
        }
        //删除子评论，不考虑事务
        QueryWrapper<Comment> child = new QueryWrapper<>();
        wrapper.eq("father_id" , commentId);
        baseMapper.delete(child);
    }

    //增加评论
    @Override
    public void addComment(Comment comment) {
        int insert = baseMapper.insert(comment);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "增加评论失败");
        }
        //向rabbitmq发送消息
        sendReplyNews(comment);
    }

    //查找文章评论,分页查找
    @Async
    @Override
    public Future<List<OneCommentVo>> findArticleComment(String articleId , Long current , Long limit) {

        //计算分页
        current = (current - 1) * limit;

        //查询一级评论和二级评论
        List<OneCommentVo> oneCommentVoList = baseMapper.findOneCommentVoByArticleId(articleId , current , limit);
        //如果评论为空，则直接返回
        if(oneCommentVoList == null || oneCommentVoList.size() == 0){
            return new AsyncResult<>(oneCommentVoList);
        }

        //设置vip标识，这里设置一级评论
        VipUtils.setVipLevel(oneCommentVoList , oneCommentVoList.get(0));

        List<TwoCommentVo> twoCommentVos = new ArrayList<>();
        //取出所有二级评论
        for(OneCommentVo oneCommentVo : oneCommentVoList) {
            twoCommentVos.addAll(oneCommentVo.getChildList());
        }

        //设置二级评论vip标识
        if(twoCommentVos.size() != 0){
            VipUtils.setVipLevel(twoCommentVos , twoCommentVos.get(0));
        }

        return new AsyncResult<>(oneCommentVoList);
    }

    //查找指定文章一级评论数量
    @Override
    public Integer findArticleCommentNumber(String articleId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        //如果是一级评论，那么father_id为空字符串
        wrapper.eq("father_id" , "");
        return baseMapper.selectCount(wrapper);
    }

    //用户发表评论之后，通知消息的我的消息
    @Async
    @Override
    public void sendReplyNews(Comment comment) {

        //取出评论者id
        String userId = comment.getUserId();
        //取出评论的文章id
        String articleId = comment.getArticleId();
        //查询出文章
        Article article = articleMapper.selectById(articleId);
        //如果文章不存在，直接返回
        if(article == null){
            return;
        }

        String title = article.getTitle();
        String replyUserId = null;
        //如果是一级评论，说明回复的是文章所有者
        if(StringUtils.isEmpty(comment.getFatherId())){
            replyUserId = article.getUserId();
        }else {
            //如果不是一级评论
            replyUserId = comment.getReplyUserId();
        }

        //如果评论者和他回复的评论或者文章所有者一样，不发送消息
        if(replyUserId.equals(userId)){
            return;
        }

        //进行数据封装
        InfoReplyMeVo infoReplyMeVo = new InfoReplyMeVo();
        infoReplyMeVo.setContent(comment.getContent());
        infoReplyMeVo.setReplyUserId(userId);
        infoReplyMeVo.setReplyUserAvatar(comment.getUserAvatar());
        infoReplyMeVo.setReplyUserNickname(comment.getUserNickname());
        infoReplyMeVo.setArticleId(articleId);
        infoReplyMeVo.setUserId(replyUserId);
        infoReplyMeVo.setTitle(title);

        //向rabbitmq发送消息
        msgProducer.sendReplyMeMsg(JSON.toJSONString(infoReplyMeVo));
    }

    //删除文章评论,不考虑事务
    @Override
    public void deleteCommentByArticleId(String articleId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        baseMapper.delete(wrapper);
    }

    //查询文章评论数量
    @Override
    public Integer findArticleAllCommentNumber(String articleId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        return baseMapper.selectCount(wrapper);
    }

    //查询用户评论数量
    @Override
    public Integer findUserAllCommentNumber(String userId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }
}
