package com.kuang.bbs.controller;


import com.kuang.bbs.entity.Comment;
import com.kuang.bbs.entity.vo.OneCommentVo;
import com.kuang.bbs.entity.vo.UserOneCommentVo;
import com.kuang.bbs.entity.vo.UserTwoCommentVo;
import com.kuang.bbs.service.CommentService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
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
import java.util.concurrent.Future;


@RestController
@RequestMapping("/bbs/comment")
@Slf4j
public class CommentController {

    @Resource
    private CommentService commentService;


    //删除评论接口
    @PostMapping("delete")
    public R delete(String commentId , HttpServletRequest request){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(commentId) || userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        commentService.deleteComment(commentId , userId);
        return R.ok();
    }


    //增加一级评论
    @PostMapping("createFirst")
    public R createFirst(UserOneCommentVo userOneCommentVo , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        //下面属于数据验证
        if(userId == null || StringUtils.isEmpty(userOneCommentVo.getUserAvatar()) || StringUtils.isEmpty(userOneCommentVo.getUserNickname()) || StringUtils.isEmpty(userOneCommentVo.getArticleId()) || StringUtils.isEmpty(userOneCommentVo.getContent())){
            throw new XiaoXiaException(ResultCode.ERROR , "增加评论失败");
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(userOneCommentVo , comment);
        comment.setUserId(userId);
        commentService.addComment(comment);
        return R.ok().data("commentId" , comment.getId());
    }

    //增加二级评论
    @PostMapping("createSecond")
    public R addComment(UserTwoCommentVo userTwoCommentVo , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        //下面属于数据验证
        if(userId == null || StringUtils.isEmpty(userTwoCommentVo.getUserAvatar()) || StringUtils.isEmpty(userTwoCommentVo.getUserNickname()) || StringUtils.isEmpty(userTwoCommentVo.getArticleId()) || StringUtils.isEmpty(userTwoCommentVo.getContent()) || StringUtils.isEmpty(userTwoCommentVo.getFatherId()) || StringUtils.isEmpty(userTwoCommentVo.getReplyUserId()) || StringUtils.isEmpty(userTwoCommentVo.getReplyUserNickname())){
            throw new XiaoXiaException(ResultCode.ERROR , "增加评论失败");
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(userTwoCommentVo , comment);
        comment.setUserId(userId);
        commentService.addComment(comment);
        return R.ok().data("commentId" , comment.getId());
    }

    //查找文章评论,分页查找
    @GetMapping("findArticleComment")
    public R findArticleComment(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                                @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                                String articleId){
        Future<List<OneCommentVo>> articleComment = commentService.findArticleComment(articleId, current, limit);
        Integer total = commentService.findArticleCommentNumber(articleId);
        List<OneCommentVo> oneCommentVoList = null;
        try {
            oneCommentVoList = articleComment.get();
        }catch(Exception e){
            throw new XiaoXiaException(ResultCode.ERROR , "查找评论失败");
        }

        return R.ok().data("commentList" , oneCommentVoList).data("total" , total);
    }

}

