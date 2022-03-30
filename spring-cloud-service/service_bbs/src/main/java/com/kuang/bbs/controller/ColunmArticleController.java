package com.kuang.bbs.controller;


import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.vo.ColumnArticleDetailVo;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import com.kuang.bbs.entity.vo.PulishColumnArticleVo;
import com.kuang.bbs.entity.vo.UpdateColumnArticleVo;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.ColunmArticleService;
import com.kuang.bbs.service.CommentService;
import com.kuang.bbs.service.LabelService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;


@RestController
@RequestMapping("/bbs/carticle")
@Slf4j
public class ColunmArticleController {

    @Resource
    private ColunmArticleService colunmArticleService;

    @Resource
    private LabelService labelService;

    @Resource
    private ArticleService articleService;

    @Resource
    private CommentService commentService;


    //删除专栏文章
    @PostMapping("deleteArticle")
    public R deleteArticle(HttpServletRequest request , String articleId , String cloumnId){
         //校验数据
         String userId = JwtUtils.getMemberIdByJwtToken(request);
         if(userId == null || StringUtils.isEmpty(articleId) || StringUtils.isEmpty(cloumnId)){
             throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
         }
         colunmArticleService.deleteArticle(userId , articleId , cloumnId);
         //标签和评论的删除
         labelService.deleteArticleLabel(articleId);
         commentService.deleteCommentByArticleId(articleId);
         return R.ok();
    }

    //发布专栏文章
    @PostMapping("publishArticle")
    public R publishArticle(PulishColumnArticleVo pulishColumnArticleVo ,
                            String[] labelList ,
                            String nickname ,
                            String avatar ,
                            HttpServletRequest request , //五个参数组成文章具体数据
                            String conlumnId  //专栏id
                            ){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(conlumnId) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(avatar)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }

        if(StringUtils.isEmpty(pulishColumnArticleVo.getTitle()) ||
                StringUtils.isEmpty(pulishColumnArticleVo.getContent()) ||
                StringUtils.isEmpty(pulishColumnArticleVo.getCategoryId()) ||
                StringUtils.isEmpty(pulishColumnArticleVo.getDescription())){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }


        //发布文章
        Article article = colunmArticleService.publishArticle(pulishColumnArticleVo , conlumnId , userId , nickname , avatar);

        //插入标签
        labelService.addArticleLabel(article.getId() , Arrays.asList(labelList));

        //数据进行相应封装
        ColumnArticleVo columnArticleVo = new ColumnArticleVo();
        columnArticleVo.setArticleId(article.getId());
        columnArticleVo.setTitle(article.getTitle());
        columnArticleVo.setSort(0L);
        return R.ok().data("columnArticle" , columnArticleVo);
    }


    //修改专栏文章的排序
    @PostMapping("updateArticleSort")
    public R updateArticleSort(Long sort , String articleId , HttpServletRequest request){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || sort == null || sort < 0 || StringUtils.isEmpty(articleId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        articleService.updateArticleSort(articleId , userId , sort);
        return R.ok();
    }

    //查询专栏文章详细数据
    @GetMapping("findColunmArticleDetail")
    public R findColunmArticleDetail(HttpServletRequest request ,
                                     String columnId ,
                                     String articleId){
        //进行数据校验
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(columnId) || StringUtils.isEmpty(articleId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确访问");
        }
        //检查是否可以访问
        Future<Boolean> booleanFuture = colunmArticleService.checkUserAbility(userId, columnId, articleId);


        ColumnArticleDetailVo columnArticleDetailVo = articleService.findColunmArticle(articleId);
        List<String> labelList = labelService.findArticleLabel(articleId);


        boolean flag = false;
        try {
            flag = booleanFuture.get();
        }catch(Exception e){
            log.warn("校验是否可以访问文章查询失败");
        }

        if(!flag){
            throw new XiaoXiaException(ResultCode.ERROR , "您没有权限访问");
        }

        return R.ok().data("labelList" , labelList).data("article" , columnArticleDetailVo);
    }


    //查询用户专栏文章，以作修改
    @GetMapping("findUserColumnArticle")
    public R findUserColumnArticle(HttpServletRequest request ,
                                   String columnId ,
                                   String articleId){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(columnId) || StringUtils.isEmpty(articleId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确访问");
        }

        //检查是否可以修改
        Future<Boolean> booleanFuture = colunmArticleService.checkUserColumnArticle(userId, columnId, articleId);

        //查询文章标签
        List<String> labelList = labelService.findArticleLabel(articleId);

        //查询文章数据
        UpdateColumnArticleVo updateColumnArticleVo = new UpdateColumnArticleVo();
        Article article = articleService.getById(articleId);
        BeanUtils.copyProperties(article , updateColumnArticleVo);
        updateColumnArticleVo.setArticleId(article.getId());


        boolean flag = false;
        try {
            flag = booleanFuture.get();
        }catch(Exception e){
            log.warn("校验是否可以修改文章失败");
        }
        if(!flag){
            throw new XiaoXiaException(ResultCode.ERROR , "您没有权限访问");
        }



        return R.ok().data("labelList" , labelList).data("article" , updateColumnArticleVo);
    }


    //修改专栏文章
    @PostMapping("updateCloumArticle")
    public R updateCloumArticle(HttpServletRequest request ,
                                String columnId ,
                                UpdateColumnArticleVo updateColumnArticleVo ,
                                String[] labelList){
        //校验数据
        String articleId = updateColumnArticleVo.getArticleId();
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(columnId) || StringUtils.isEmpty(articleId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确访问");
        }

        //检查是否可以修改
        Future<Boolean> booleanFuture = colunmArticleService.checkUserColumnArticle(userId, columnId, articleId);
        boolean flag = false;
        try {
            flag = booleanFuture.get();
        }catch(Exception e){
            log.warn("校验是否可以修改文章失败");
        }
        if(!flag){
            throw new XiaoXiaException(ResultCode.ERROR , "您没有权限修改");
        }

        colunmArticleService.updateCloumArticle(updateColumnArticleVo);
        labelService.addArticleLabel(articleId , Arrays.asList(labelList));

        return R.ok();
    }
}

