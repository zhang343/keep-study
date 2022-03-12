package com.kuang.bbs.controller;


import com.kuang.bbs.client.UcenterClient;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.vo.ArticleUpdateAndCreateVo;
import com.kuang.bbs.entity.vo.ArticleVo;
import com.kuang.bbs.entity.vo.UserArticleVo;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        if(!StringUtils.isEmpty(articleId) || !StringUtils.isEmpty(content)){
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

        Future<ArticleVo> articleDetail = articleService.findArticleDetail(articleId , userId);
        Future<List<String>> articleLabel = labelService.findArticleLabel(articleId);
        ArticleVo articleVo = null;
        boolean isCollection = false;
        if(userId != null){
            R ucenterR = ucenterClient.findUserIsCollection(articleId);
            Object o = ucenterR.getData().get("isCollection");
            if(o != null){
                isCollection = (boolean) o;
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

        try {
            articleVo = articleDetail.get();
        }catch(Exception e){
            log.error("查询文章数据异常,文章id:" + articleId);
            throw new XiaoXiaException(ResultCode.ERROR , "查询文章失败");
        }
        return R.ok().data("isCollection" , isCollection).data("labelList" , labelList).data("article" , articleVo);
    }


    //用户发布文章,指江湖文章的发布
    @PostMapping("create")
    public R create(ArticleUpdateAndCreateVo articleUpdateAndCreateVo , List<String> labelList , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        String token = request.getHeader("token");
        log.info("用户发布文章,用户id:" + userId);
        if(userId == null){
            log.warn("用户非法发布文章,用户id:" + null);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Article article = articleService.addArticle(articleUpdateAndCreateVo, userId);
        //删除用户缓存
        RedisUtils.delKey(userId);
        String articleId = article.getId();
        //插入文章标签
        labelService.addArticleLabel(articleId , labelList);
        //如果文章发布，向好友动态发送消息
        if(article.getIsRelease()){
            articleService.sendFrientFeed(article.getId() , token);
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
    public R update(ArticleUpdateAndCreateVo articleUpdateAndCreateVo , List<String> labelList , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        String token = request.getHeader("token");
        String articleId = articleUpdateAndCreateVo.getId();
        log.info("用户修改文章,用户id:" + userId + ",文章id:" + articleId);
        if(userId == null || StringUtils.isEmpty(articleId)){
            log.warn("用户非法修改文章,用户id:" + userId + ",文章id:" + articleId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Article article = articleService.updateArticle(articleUpdateAndCreateVo, userId);
        //插入文章标签
        labelService.addArticleLabel(articleId , labelList);
        //检测文章以前没有发布，现在却发布了
        if(articleUpdateAndCreateVo.getIsRelease() && !article.getIsRelease()){
            //发送好友动态
            articleService.sendFrientFeed(article.getId() , token);
        }
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
        //删除文章标签
        labelService.deleteArticleLabel(articleId);
        return R.ok();
    }

    //查找用户所有文章数量
    @GetMapping("findUserArticleNumber")
    public R findUserArticleNumber(HttpServletRequest request){
        log.info("查找用户所有文章数量");
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Integer articleNumber = articleService.findArticleNumberByUserId(userId);
        return R.ok().data("articleNumber" , articleNumber);
    }

    //查询用户已经发布和没有违规的文章数量
    @GetMapping("findUserReleaseArticleNumber")
    public R findUserReleaseArticleNumber(HttpServletRequest request){
        log.info("查询用户已经发布和没有违规的文章数量");
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Integer releaseArticleNumber = articleService.findReleaseArticleNumber(userId);
        return R.ok().data("releaseArticleNumber" , releaseArticleNumber);
    }

    //查询用户已经发布和没有违规的文章数量和评论数量
    @GetMapping("findURANAndCN")
    public R findURANAndCN(HttpServletRequest request){
        log.info("查询用户已经发布和没有违规的文章数量和评论数量");
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Integer releaseArticleNumber = articleService.findReleaseArticleNumber(userId);
        Integer commentNumber = commentService.findUserCommentNumber(userId);
        return R.ok().data("releaseArticleNumber" , releaseArticleNumber)
                .data("commentNumber" , commentNumber);

    }

    //查找文章浏览量
    @GetMapping("findArticleViews")
    public R findArticleViews(@RequestParam("articleIdList") List<String> articleIdList){
        if(articleIdList == null || articleIdList.size() == 0){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确查询");
        }
        Map<String , Object> map = articleService.findArticleViews(articleIdList);
        return R.ok().data(map);
    }

    //查询用户我的文章（这里查询我的文章数量和我的文章收藏数量）
    @GetMapping("findMyArticleAndCollection")
    public R findMyArticleAndCollection(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                                        @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                                        HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        String token = request.getHeader("token");
        log.info("查询用户我的文章（这里查询我的文章数量和我的文章收藏数量）,用户id" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Future<Integer> userCollectionNumber = articleService.findUserCollectionNumber(token);
        Integer total = articleService.findArticleNumberByUserId(userId);
        List<UserArticleVo> userArticleVos = articleService.findMyArticle(current , limit , userId);
        Integer collectionNumber = 0;
        if(userCollectionNumber.isDone()){
            try {
                collectionNumber = userCollectionNumber.get(100 , TimeUnit.MILLISECONDS);
            }catch(Exception e){
                log.warn("查询用户文章收藏数量失败");
            }
        }
        return R.ok().data("collectionNumber" , collectionNumber).data("total" , total).data("articleList" , userArticleVos);
    }
}

