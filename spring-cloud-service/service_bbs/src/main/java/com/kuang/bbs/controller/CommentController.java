package com.kuang.bbs.controller;


import com.alibaba.fastjson.JSON;
import com.kuang.bbs.client.UcenterClient;
import com.kuang.bbs.entity.Comment;
import com.kuang.bbs.entity.vo.OneCommentVo;
import com.kuang.bbs.entity.vo.UserOneCommentVo;
import com.kuang.bbs.entity.vo.UserTwoCommentVo;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.CommentService;
import com.kuang.springcloud.entity.InfoReplyMeVo;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.rabbitmq.MsgProducer;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@RestController
@RequestMapping("/bbs/comment")
@Slf4j
public class CommentController {

    @Resource
    private CommentService commentService;


    //删除评论接口
    @PostMapping("delete")
    public R delete(String commentId , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户删除评论,评论id:" + commentId + ",用户id:" + userId);
        if(StringUtils.isEmpty(commentId) || userId == null){
            log.warn("有人进行非法操作删除评论");
            throw new XiaoXiaException(ResultCode.ERROR , "删除评论失败");
        }
        commentService.deleteComment(commentId , userId);
        return R.ok();
    }


    //增加一级评论
    @PostMapping("createFirst")
    public R createFirst(UserOneCommentVo userOneCommentVo , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        //下面属于数据验证
        if(userId == null || StringUtils.isEmpty(userOneCommentVo.getArticleId()) || StringUtils.isEmpty(userOneCommentVo.getContent())){
            log.warn("有人非法增加评论");
            throw new XiaoXiaException(ResultCode.ERROR , "增加评论失败");
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(userOneCommentVo , comment);
        comment.setUserId(userId);
        commentService.addComment(comment);
        return R.ok();
    }

    //增加二级评论
    @PostMapping("createSecond")
    public R addComment(UserTwoCommentVo userTwoCommentVo , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        //下面属于数据验证
        if(userId == null || StringUtils.isEmpty(userTwoCommentVo.getArticleId()) || StringUtils.isEmpty(userTwoCommentVo.getContent()) || StringUtils.isEmpty(userTwoCommentVo.getFatherId()) || StringUtils.isEmpty(userTwoCommentVo.getReplyUserId()) || StringUtils.isEmpty(userTwoCommentVo.getReplyUserNickname())){
            log.warn("有人非法增加评论");
            throw new XiaoXiaException(ResultCode.ERROR , "增加评论失败");
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(userTwoCommentVo , comment);
        comment.setUserId(userId);
        commentService.addComment(comment);
        return R.ok();
    }

    //查找文章评论,分页查找
    @GetMapping("findArticleComment")
    public R findArticleComment(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                                @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                                String articleId){
        log.info("开始进行查找文章评论,分页查找,文章id：" + articleId);
        Future<List<OneCommentVo>> articleComment = commentService.findArticleComment(articleId, current, limit);
        Integer total = commentService.findArticleCommentNumber(articleId);
        List<OneCommentVo> oneCommentVoList = null;
        try {
            oneCommentVoList = articleComment.get();
        }catch(Exception e){
            log.error("查找评论失败");
            throw new XiaoXiaException(ResultCode.ERROR , "查找评论失败");
        }

        return R.ok().data("commentList" , oneCommentVoList).data("total" , total);
    }

}

