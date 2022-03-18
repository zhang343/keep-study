package com.kuang.bbs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.bbs.client.UcenterClient;
import com.kuang.bbs.client.VipClient;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.Comment;
import com.kuang.bbs.entity.vo.OneCommentVo;
import com.kuang.bbs.entity.vo.TwoCommentVo;
import com.kuang.bbs.mapper.ArticleMapper;
import com.kuang.bbs.mapper.CommentMapper;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.CommentService;
import com.kuang.springcloud.entity.InfoReplyMeVo;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.rabbitmq.MsgProducer;
import com.kuang.springcloud.utils.R;
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

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private MsgProducer msgProducer;

    @Resource
    private UcenterClient ucenterClient;

    //查询系统评论数量
    @Cacheable(value = "commentNumber")
    @Override
    public Integer findCommentNumber() {
        log.info("查询系统评论数量");
        return baseMapper.selectCount(null);
    }

    //用户删除评论
    @Override
    public void deleteComment(String commentId, String userId) {
        log.info("用户删除评论,评论id:" + commentId + ",用户id:" + userId);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("id" , commentId);
        wrapper.eq("user_id" , userId);
        int delete = baseMapper.delete(wrapper);
        if(delete != 1){
            log.error("用户删除评论失败,评论id:" + commentId + ",用户id:" + userId);
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
        String articleId = comment.getArticleId();
        log.info("增加评论,文章id:" + articleId);
        //进行远程调用查找插入评论的用户头像和昵称
        R ucenterR = ucenterClient.findAvatarAndNicknameByUserId(comment.getUserId());
        if(!ucenterR.getSuccess()){
            log.warn("远程调用失败，无法查询出用户头像和昵称");
            throw new XiaoXiaException(ResultCode.ERROR , "增加评论失败");
        }
        String avatar = (String) ucenterR.getData().get("avatar");
        String nickname = (String) ucenterR.getData().get("nickname");
        comment.setUserAvatar(avatar);
        comment.setUserNickname(nickname);
        int insert = baseMapper.insert(comment);
        if(insert != 1){
            log.info("增加评论失败，文章id：" + articleId);
            throw new XiaoXiaException(ResultCode.ERROR , "增加评论失败");
        }
        //向rabbitmq发送消息
        sendReplyNews(comment);
    }

    //查找文章评论,分页查找
    @Async
    @Override
    public Future<List<OneCommentVo>> findArticleComment(String articleId , Long current , Long limit) {
        log.info("开始进行查找文章评论,分页查找,文章id：" + articleId);
        //计算分页
        current = (current - 1) * limit;
        List<OneCommentVo> oneCommentVoList = baseMapper.findOneCommentVoByArticleId(articleId , current , limit);
        if(oneCommentVoList == null || oneCommentVoList.size() == 0){
            return new AsyncResult<>(oneCommentVoList);
        }
        Set<String> userIdSet = new HashSet<>();
        //取出所有评论用户的id
        for(OneCommentVo oneCommentVo : oneCommentVoList){
            userIdSet.add(oneCommentVo.getUserId());
            for(TwoCommentVo twoCommentVo : oneCommentVo.getChildList()){
                userIdSet.add(twoCommentVo.getUserId());
            }
        }
        List<String> userIdList = new ArrayList<>(userIdSet);
        Map<String , String> userVipLevel = VipUtils.getUserVipLevel(userIdList);
        if(userVipLevel == null){
            return new AsyncResult<>(oneCommentVoList);
        }
        for(OneCommentVo oneCommentVo : oneCommentVoList){
            oneCommentVo.setUserVipLevel(userVipLevel.get(oneCommentVo.getUserId()));
            for(TwoCommentVo twoCommentVo : oneCommentVo.getChildList()){
                twoCommentVo.setUserVipLevel(userVipLevel.get(twoCommentVo.getUserId()));
            }
        }
        return new AsyncResult<>(oneCommentVoList);
    }

    //查找指定文章一级评论数量
    @Override
    public Integer findArticleCommentNumber(String articleId) {
        log.info("查找指定文章一级评论数量,文章id:" + articleId);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        wrapper.eq("father_id" , "");
        return baseMapper.selectCount(wrapper);
    }

    //用户发表评论之后，向rabbitmq发送消息
    @Async
    @Override
    public void sendReplyNews(Comment comment) {
        log.info("用户发表评论之后，向rabbitmq发送消息");
        String userId = comment.getUserId();
        Article article = articleMapper.selectById(comment.getArticleId());
        if(article == null){
            return;
        }
        String title = article.getTitle();
        String replyUserId = null;
        if(StringUtils.isEmpty(comment.getFatherId())){
            //说明找文章
            replyUserId = article.getUserId();
        }else {
            //说明找评论
            replyUserId = comment.getReplyUserId();
        }
        //如果评论者和他回复的评论或者文章所有者一样，不发送消息
        if(replyUserId.equals(userId)){
            return;
        }
        log.info("开始向rabbitmq发送数据,存储回复记录,用户id:" + userId);
        InfoReplyMeVo infoReplyMeVo = new InfoReplyMeVo();
        infoReplyMeVo.setContent(comment.getContent());
        infoReplyMeVo.setReplyUserId(userId);
        infoReplyMeVo.setReplyUserAvatar(comment.getUserAvatar());
        infoReplyMeVo.setReplyUserNickname(comment.getUserNickname());
        infoReplyMeVo.setArticleId(comment.getArticleId());
        infoReplyMeVo.setUserId(replyUserId);
        infoReplyMeVo.setTitle(title);
        try {
            msgProducer.sendReplyMeMsg(JSON.toJSONString(infoReplyMeVo));
        }catch(Exception e){
            log.warn("开始向rabbitmq发送数据失败,存储回复记录,用户id:" + userId);
        }
    }

    //删除文章评论
    @Async
    @Override
    public void deleteCommentByArticleId(String articleId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        baseMapper.delete(wrapper);
    }

    //查询文章所有评论数量
    @Override
    public Integer findArticleAllCommentNumber(String articleId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        return baseMapper.selectCount(wrapper);
    }
}
