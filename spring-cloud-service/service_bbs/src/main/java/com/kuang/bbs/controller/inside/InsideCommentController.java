package com.kuang.bbs.controller.inside;

import com.kuang.bbs.service.CommentService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inside/comment")
@Slf4j
public class InsideCommentController {

    @Resource
    private CommentService commentService;

    //查询用户所有评论数量
    @GetMapping("findUserCommentNumber")
    public R findUserCommentNumber(String userId){
        Integer commentNumber = commentService.findUserCommentNumber(userId);
        return R.ok().data("commentNumber" , commentNumber);
    }
}
