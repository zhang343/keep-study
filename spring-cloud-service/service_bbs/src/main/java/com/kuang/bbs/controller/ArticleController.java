package com.kuang.bbs.controller;


import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.vo.ArticleUpdateAndCreateVo;
import com.kuang.bbs.entity.vo.ArticleVo;
import com.kuang.bbs.entity.vo.OtherUserArticleVo;
import com.kuang.bbs.entity.vo.UserArticleVo;
import com.kuang.bbs.service.*;
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
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/bbs/article")
@Slf4j
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ReportService reportService;

    @Resource
    private LabelService labelService;

    @Resource
    private CommentService commentService;

    @Resource
    private CollectService collectService;


    //举报文章接口
    @PostMapping("report")
    public R report(String articleId , String[] content){
        //进行数据校验，保证articleId和content不为空
        //校验成功，开启异步执行举报
        if(!StringUtils.isEmpty(articleId) && content != null && content.length != 0){
            reportService.report(articleId , content);
        }
        return R.ok();
    }


    //查询文章的详细数据，这里面会根据用户是否是文章创建者来进行相应的处理
    @GetMapping("detail")
    public R detail(String articleId , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        //校验数据，保证文章id不为空
        if(StringUtils.isEmpty(articleId)){
            throw new XiaoXiaException(ResultCode.ERROR , "该文章不存在，请不要查找");
        }
        //查询文章详细数据
        ArticleVo articleVo = articleService.findArticleDetail(articleId , userId);
        //设置文章浏览量缓存
        articleService.setArticleViews(articleId , request.getRemoteAddr());
        //查询文章标签
        List<String> articleLabel = labelService.findArticleLabel(articleId);
        //查询文章评论数量
        Integer commentNumber = commentService.findArticleAllCommentNumber(articleId);
        //查询用户是否收藏了该文章
        boolean isCollection = false;
        if(userId != null){
            isCollection = collectService.findUserIsCollection(articleId , userId);
        }


        long setSize = RedisUtils.getSetSize(articleVo.getId());
        articleVo.setViews(setSize + articleVo.getViews());
        return R.ok().data("commentNumber" , commentNumber).
                data("isCollection" , isCollection).
                data("labelList" , articleLabel).
                data("article" , articleVo);
    }



    //用户发布文章,指江湖文章的发布
    @PostMapping("create")
    public R create(ArticleUpdateAndCreateVo articleUpdateAndCreateVo ,
                    String avatar , String nickname , String[] labelList ,
                    HttpServletRequest request){

        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        Article article = articleService.addArticle(articleUpdateAndCreateVo, avatar , nickname , userId , labelList);
        //删除用户缓存
        RedisUtils.delKey(userId + "article");
        String articleId = article.getId();
        //插入文章标签
        labelService.addArticleLabel(articleId , Arrays.asList(labelList));

        //发送到好友动态
        Boolean isRelease = articleUpdateAndCreateVo.getIsRelease();
        if(isRelease){
            articleService.sendFrientFeed(article , userId);
        }
        return R.ok().data("articleId" , articleId);
    }


    //文章数据的查询,配合文章数据的修改
    @GetMapping("find")
    public R find(String articleId , HttpServletRequest request){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(articleId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确查询");
        }

        ArticleUpdateAndCreateVo article = articleService.findArticleByArticleIdAndUserId(articleId , userId);
        List<String> labelList = labelService.findArticleLabel(articleId);
        return R.ok().data("labelList" , labelList).data("article" , article);
    }

    //用户修改文章
    @PostMapping("update")
    public R update(ArticleUpdateAndCreateVo articleUpdateAndCreateVo ,
                    String[] labelList  , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        String articleId = articleUpdateAndCreateVo.getId();

        //校验文章数据
        if(userId == null || StringUtils.isEmpty(articleId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        articleService.updateArticle(articleUpdateAndCreateVo, userId , labelList);
        //插入文章标签
        labelService.addArticleLabel(articleId , Arrays.asList(labelList));
        return R.ok();
    }

    //用户删除文章,如果是江湖文章是真删除，如果是专栏文章同步到江湖的，将会改为专栏文章，不同步到江湖
    @PostMapping("delete")
    public R delete(String articleId , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        //校验数据
        if(userId == null || StringUtils.isEmpty(articleId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        articleService.deleteArticle(articleId , userId);
        return R.ok();
    }


    //查找用户在江湖的所有文章
    @GetMapping("findUserArticle")
    public R findUserArticle(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                             @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                             HttpServletRequest request){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        Integer total = articleService.findUserAllArticleNumber(userId);
        List<UserArticleVo> userArticleVoList = articleService.findUserAllArticle(userId , current , limit);
        return R.ok().data("total" , total).data("userBbsArticleList" , userArticleVoList);
    }

    //查询他人在江湖发布的文章
    @GetMapping("findOtherUserArticle")
    public R findOtherUserArticle(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                                  @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                                  String userId){
        //校验数据
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        Integer total = articleService.findUserbbsArticleNumber(userId);
        List<OtherUserArticleVo> otherUserArticleVos = articleService.findOtherUserArticle(userId , current , limit);
        return R.ok().data("total" , total).data("otherUserArticleList" , otherUserArticleVos);
    }
}

