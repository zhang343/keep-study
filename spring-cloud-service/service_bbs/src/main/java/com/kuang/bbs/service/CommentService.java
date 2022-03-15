package com.kuang.bbs.service;

import com.kuang.bbs.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.vo.OneCommentVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface CommentService extends IService<Comment> {

    //查询系统评论数量
    Integer findCommentNumber();

    //用户删除评论
    void deleteComment(String commentId, String userId);

    //增加评论
    void addComment(Comment comment);

    //查找文章评论,分页查找
    Future<List<OneCommentVo>> findArticleComment(String articleId , Long current , Long limit);

    //查找指定文章一级评论数量
    Integer findArticleCommentNumber(String articleId);

    //查找回复用户的id和文章标题,
    Map<String, String> findReplyUserIdAndArticleTitle(String replyCommentId , String articleId);

    //用户发表评论之后，向rabbitmq发送消息
    void sendReplyNews(Comment comment);

    //查找用户评论数量
    Integer findUserCommentNumber(String userId);

    //删除文章评论
    void deleteCommentByArticleId(String articleId);

    //查询文章所有评论数量
    Integer findArticleAllCommentNumber(String articleId);
}
