package com.kuang.bbs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.bbs.client.VipClient;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.Comment;
import com.kuang.bbs.entity.vo.OneCommentVo;
import com.kuang.bbs.entity.vo.TwoCommentVo;
import com.kuang.bbs.mapper.CommentMapper;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.CommentService;
import com.kuang.springcloud.entity.InfoReplyMeVo;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.rabbitmq.MsgProducer;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

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
    private VipClient vipClient;


    @Resource
    private ArticleService articleService;

    @Resource
    private MsgProducer msgProducer;

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
        baseMapper.delete(wrapper);
    }

    //增加评论
    @Override
    public void addComment(Comment comment) {
        log.info("增加评论,文章id:" + comment.getArticleId());
        int insert = baseMapper.insert(comment);
        if(insert != 1){
            log.info("增加一级评论失败，文章id：" + comment.getArticleId());
            throw new XiaoXiaException(ResultCode.ERROR , "增加评论失败");
        }

    }

    //查找文章评论,分页查找
    @Async
    @Override
    public Future<List<OneCommentVo>> findArticleComment(String articleId , Long current , Long limit) {
        log.info("开始进行查找文章评论,分页查找,文章id：" + articleId);
        //计算分页
        current = (current - 1) * limit;
        List<OneCommentVo> oneCommentVoList = baseMapper.findOneCommentVoByArticleId(articleId , current , limit);
        Set<String> userIdSet = new HashSet<>();
        //取出所有评论用户的id
        for(OneCommentVo oneCommentVo : oneCommentVoList){
            userIdSet.add(oneCommentVo.getUserId());
            for(TwoCommentVo twoCommentVo : oneCommentVo.getChildList()){
                userIdSet.add(twoCommentVo.getUserId());
            }
        }

        List<String> userIdList = new ArrayList<>(userIdSet);
        if(userIdList.size() == 0){
            return new AsyncResult<>(oneCommentVoList);
        }
        //远程调用获取用户viplogo
        log.info("远程调用service-vip下面的/vm/user/findMemberRightLogo,获取用户viplogo,用户:" + userIdList);
        R vipR = vipClient.findMemberRightLogo(userIdList);
        if(vipR.getSuccess()){
            Map<String , Object> logo = vipR.getData();
            for(OneCommentVo oneCommentVo : oneCommentVoList){
                oneCommentVo.setUserVipLevel((String) logo.get(oneCommentVo.getUserId()));
                for(TwoCommentVo twoCommentVo : oneCommentVo.getChildList()){
                    twoCommentVo.setUserVipLevel((String) logo.get(twoCommentVo.getUserId()));
                }
            }
        }
        return new AsyncResult<>(oneCommentVoList);
    }

    //查找指定文章一级评论数量
    @Async
    @Override
    public Future<Integer> findArticleCommentNumber(String articleId) {
        log.info("查找指定文章一级评论数量,文章id:" + articleId);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        wrapper.eq("father_id" , "");
        return new AsyncResult<>(baseMapper.selectCount(wrapper));
    }

    //查找回复用户的id和文章标题
    @Override
    public Map<String, String> findReplyUserIdAndArticleTitle(String replyCommentId, String articleId) {
        log.info("查找回复用户的id和文章标题,评论id");
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.select("user_id");
        wrapper.eq("id" , replyCommentId);
        Comment comment = baseMapper.selectOne(wrapper);
        Article article = articleService.findArticleUserIdAndTitle(articleId);
        String userId = null;
        String title = null;
        //如果为null，说明回复的应该是文章所有者
        if(comment == null){
            userId = article.getUserId();
        }else {
            userId = comment.getUserId();
        }
        title = article.getTitle();
        Map<String , String> map = new HashMap<>();
        map.put("userId" , userId);
        map.put("title" , title);
        return map;
    }

    //用户发表评论之后，向rabbitmq发送消息
    @Async
    @Override
    public void sendReplyNews(Comment comment) {
        log.info("用户发表评论之后，向rabbitmq发送消息");
        String userId = comment.getUserId();
        Map<String, String> replyUserIdAndArticleTitle = findReplyUserIdAndArticleTitle(comment.getFatherId(), comment.getArticleId());
        String replyUserId = replyUserIdAndArticleTitle.get("userId");
        //如果评论者和他回复的评论或者文章所有者一样，不发送消息
        if(replyUserId.equals(userId)){
            return;
        }
        String title = replyUserIdAndArticleTitle.get("title");
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

    //查找用户评论数量
    @Override
    public Integer findUserCommentNumber(String userId) {
        log.info("查找用户评论数量");
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }
}
