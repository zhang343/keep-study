package com.kuang.bbs.controller;


import com.kuang.bbs.client.UcenterClient;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.vo.ArticleUpdateAndCreateVo;
import com.kuang.bbs.entity.vo.ArticleVo;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.CommentService;
import com.kuang.bbs.service.LabelService;
import com.kuang.bbs.service.ReportService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 * 文章处理类
 */
@RestController
@RequestMapping("/bbs/article")
@Slf4j
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ReportService reportService;

    @Resource
    private UcenterClient ucenterClient;

    @Resource
    private LabelService labelService;

    @Resource
    private CommentService commentService;


    //举报文章接口
    @PostMapping("report")
    public R report(String articleId , String content){
        log.info("举报文章,文章id:" + articleId + ",举报内容:" + content);
        if(!StringUtils.isEmpty(articleId) && !StringUtils.isEmpty(content)){
            reportService.report(articleId , content);
        }
        return R.ok();
    }


    //查询文章详细数据
    @GetMapping("detail")
    public R detail(String articleId , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询文章详细数据,文章id:" + articleId + ",用户id:" + userId);
        if(StringUtils.isEmpty(articleId)){
            log.warn("有人非法查找文章,文章id:" + articleId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查找文章");
        }

        ArticleVo articleVo = articleService.findArticleDetail(articleId , userId);
        Future<List<String>> articleLabel = labelService.findArticleLabel(articleId);
        Integer commentNumber = commentService.findArticleAllCommentNumber(articleId);
        boolean isCollection = false;
        if(userId != null){
            R ucenterR = ucenterClient.findUserIsCollection(articleId , userId);
            if(ucenterR.getSuccess()){
                isCollection = (boolean) ucenterR.getData().get("isCollection");
            }
        }

        //等待0.2秒取出结果
        List<String> labelList = null;
        try {
            labelList = articleLabel.get(200 , TimeUnit.MILLISECONDS);
        }catch(Exception e){
            log.warn("查询文章标签异常,文章id:" + articleId);
            labelList = new ArrayList<>();
        }

        //设置文章浏览量缓存
        articleService.setArticleViews(articleId , request.getRemoteAddr());
        return R.ok().data("commentNumber" , commentNumber).data("isCollection" , isCollection).data("labelList" , labelList).data("article" , articleVo);
    }


    //用户发布文章,指江湖文章的发布
    @PostMapping("create")
    public R create(ArticleUpdateAndCreateVo articleUpdateAndCreateVo , String avatar , String nickname , List<String> labelList , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户发布文章,用户id:" + userId);
        if(userId == null){
            log.warn("用户非法发布文章,用户id:" + null);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Article article = articleService.addArticle(articleUpdateAndCreateVo, avatar , nickname , userId);
        //删除用户缓存
        RedisUtils.delKey(userId);
        String articleId = article.getId();
        //插入文章标签
        labelService.addArticleLabel(articleId , labelList);
        //如果文章发布，向好友动态发送消息
        if(article.getIsRelease() != null && article.getIsRelease()){
            articleService.sendFrientFeed(articleId , userId);
        }
        return R.ok();
    }


    //文章数据的查询,配合文章数据的修改
    @GetMapping("find")
    public R find(String articleId , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("文章数据的查询,配合文章数据的修改,用户id:" + userId + ",文章id:" + articleId);
        if(userId == null || StringUtils.isEmpty(articleId)){
            log.warn("有人非法文章数据的查询,配合文章数据的修改,用户id:" + userId + ",文章id:" + articleId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        Future<List<String>> articleLabel = labelService.findArticleLabel(articleId);
        ArticleUpdateAndCreateVo article = articleService.findArticleByArticleIdAndUserId(articleId , userId);
        //等待0.2秒取出结果
        List<String> labelList = null;
        try {
            labelList = articleLabel.get(200 , TimeUnit.MILLISECONDS);
        }catch(Exception e){
            log.warn("查询文章标签异常,文章id:" + articleId);
            labelList = new ArrayList<>();
        }
        return R.ok().data("labelList" , labelList).data("article" , article);
    }

    //用户修改文章
    @PostMapping("update")
    public R update(ArticleUpdateAndCreateVo articleUpdateAndCreateVo , List<String> labelList  , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        String articleId = articleUpdateAndCreateVo.getId();
        log.info("用户修改文章,用户id:" + userId + ",文章id:" + articleId);
        if(userId == null || StringUtils.isEmpty(articleId)){
            log.warn("用户非法修改文章,用户id:" + userId + ",文章id:" + articleId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        articleService.updateArticle(articleUpdateAndCreateVo, userId);
        //插入文章标签
        labelService.addArticleLabel(articleId , labelList);
        return R.ok();
    }

    //用户删除文章,真删除,这里不管专栏文章还是江湖文章
    @PostMapping("delete")
    public R delete(String articleId , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户删除文章,用户id:" + userId + ",文章id:" + articleId);
        if(userId == null || StringUtils.isEmpty(articleId)){
            log.warn("用户非法删除文章,用户id:" + userId + ",文章id:" + articleId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        articleService.deleteArticle(articleId , userId);
        //删除文章评论
        commentService.deleteCommentByArticleId(articleId);
        //删除文章标签
        labelService.deleteArticleLabel(articleId);
        return R.ok();
    }

}

