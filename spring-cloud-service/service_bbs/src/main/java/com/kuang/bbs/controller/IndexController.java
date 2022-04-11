package com.kuang.bbs.controller;

import com.kuang.bbs.entity.vo.IndexArticleVo;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.CommentService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Future;


@RestController
@RequestMapping("/bbs/index")
@Slf4j
public class IndexController {

    @Resource
    private ArticleService articleService;

    @Resource
    private CommentService commentService;

    //查询系统前三名课程还有系统用户、评论、文章数量
    @GetMapping("getPayCourseAndUACNumber")
    public R getPayCourseAndUACNumber(){
        Integer articleNumber = articleService.findArticleNumber();
        Integer commentNumber = commentService.findCommentNumber();
        return R.ok()
                .data("courseList" , RedisUtils.getValue(RedisUtils.COURSEORDERBYPRICE))
                .data("userNumber" , RedisUtils.getValue(RedisUtils.AllUSERNUMBER))
                .data("articleNumber" , articleNumber)
                .data("commentNumber" , commentNumber);
    }

    //条件分页查询文章
    @GetMapping("pageArticleCondition")
    public R pageArticleCondition(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                                  @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                                  String categoryId ,
                                  Boolean isExcellentArticle ,
                                  String articleNameOrLabelName){
        Future<List<IndexArticleVo>> listFuture = articleService.pageArticleCondition(current, limit, categoryId, isExcellentArticle, articleNameOrLabelName);
        Long total = articleService.findArticleNumber(categoryId, isExcellentArticle, articleNameOrLabelName);
        List<IndexArticleVo> articleVoList = null;
        try {
            articleVoList = listFuture.get();
        } catch(Exception e) {
            throw new XiaoXiaException(ResultCode.ERROR , "查询文章失败");
        }
        return R.ok().data("total" , total).data("articleList" , articleVoList);
    }

}
