package com.kuang.bbs.controller;


import com.alibaba.fastjson.JSON;
import com.kuang.bbs.client.UcenterClient;
import com.kuang.bbs.entity.Comment;
import com.kuang.bbs.entity.vo.OneCommentVo;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.CommentService;
import com.kuang.springcloud.entity.InfoReplyMeVo;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.rabbitmq.MsgProducer;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
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

    @Resource
    private UcenterClient ucenterClient;


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



    //增加评论
    @PostMapping("addComment")
    public R addComment(Comment comment , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        //下面属于数据验证
        if(userId == null || StringUtils.isEmpty(comment.getArticleId()) || StringUtils.isEmpty(comment.getContent())){
            log.warn("有人非法增加评论");
            throw new XiaoXiaException(ResultCode.ERROR , "增加评论失败");
        }

        if(!StringUtils.isEmpty(comment.getFatherId())){
            if(StringUtils.isEmpty(comment.getReplyUserId()) || StringUtils.isEmpty(comment.getReplyUserNickname())){
                log.warn("有人非法增加评论");
                throw new XiaoXiaException(ResultCode.ERROR , "增加评论失败");
            }
        }else {
            comment.setFatherId(null);
        }
        //进行远程调用查找插入评论的用户头像和昵称
        R ucenterR = ucenterClient.findAvatarAndNicknameByUserId();
        if(!ucenterR.getSuccess()){
            log.warn("远程调用失败，无法查询出用户头像和昵称");
            throw new XiaoXiaException(ResultCode.ERROR , "增加评论失败");
        }
        String avatar = (String) ucenterR.getData().get("avatar");
        String nickname = (String) ucenterR.getData().get("nickname");
        comment.setUserId(userId);
        comment.setUserAvatar(avatar);
        comment.setUserNickname(nickname);
        commentService.addComment(comment);
        //向rabbitmq发送消息
        commentService.sendReplyNews(comment);
        return R.ok();
    }

    //查找文章评论,分页查找
    @GetMapping("findArticleComment")
    public R findArticleComment(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                                @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                                String articleId){
        log.info("开始进行查找文章评论,分页查找,文章id：" + articleId);
        Future<List<OneCommentVo>> articleComment = commentService.findArticleComment(articleId, current, limit);
        Future<Integer> articleCommentNumber = commentService.findArticleCommentNumber(articleId);
        List<OneCommentVo> oneCommentVoList = null;
        Integer total = 0;
        try {
            total = articleCommentNumber.get();
            oneCommentVoList = articleComment.get();
        }catch(Exception e){
            log.error("查找评论失败");
            throw new XiaoXiaException(ResultCode.ERROR , "查找评论失败");
        }

        return R.ok().data("commentList" , oneCommentVoList).data("total" , total);
    }

}

