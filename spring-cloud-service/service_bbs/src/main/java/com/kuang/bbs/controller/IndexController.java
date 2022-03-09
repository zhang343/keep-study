package com.kuang.bbs.controller;

import com.kuang.bbs.client.CourseClient;
import com.kuang.bbs.client.UcenterClient;
import com.kuang.bbs.entity.vo.ArticleVo;
import com.kuang.bbs.entity.vo.IndexArticleVo;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.CommentService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 * 首页处理类
 */
@RestController
@RequestMapping("/bbs/index")
@Slf4j
public class IndexController {

    @Resource
    private CourseClient courseClient;

    @Resource
    private UcenterClient ucenterClient;

    @Resource
    private ArticleService articleService;

    @Resource
    private CommentService commentService;

    //查询系统前三名课程还有系统用户、评论、文章数量
    @GetMapping("getPayCourseAndUACNumber")
    public R getPayCourseAndUACNumber(){
        log.info("查询系统前三名课程还有系统用户、评论、文章数量");
        R courseR = courseClient.findCourseOrderByPrice();
        R userR = ucenterClient.findUserNumber();
        Integer articleNumber = articleService.findArticleNumber();
        Integer commentNumber = commentService.findCommentNumber();
        return R.ok()
                .data("courseList" , courseR.getData().get("courseList"))
                .data("userNumber" , userR.getData().get("userNumber"))
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
        log.info("开始条件分页查询文章");
        Future<List<IndexArticleVo>> listFuture = articleService.pageArticleCondition(current, limit, categoryId, isExcellentArticle, articleNameOrLabelName);
        Future<Long> articleNumber = articleService.findArticleNumber(categoryId, isExcellentArticle, articleNameOrLabelName);
        List<IndexArticleVo> articleVoList = null;
        Long total = 0L;
        try {
            total = articleNumber.get();
            articleVoList = listFuture.get();
        } catch(Exception e) {
            log.error("根据条件查询系统文章失败");
            throw new XiaoXiaException(ResultCode.ERROR , "查询文章失败");
        }
        return R.ok().data("total" , total).data("articleList" , articleVoList);
    }

}
