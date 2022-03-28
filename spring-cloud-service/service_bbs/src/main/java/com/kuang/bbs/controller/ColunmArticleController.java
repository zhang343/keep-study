package com.kuang.bbs.controller;


import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.vo.PulishColumnArticleVo;
import com.kuang.bbs.service.ColunmArticleService;
import com.kuang.bbs.service.LabelService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-27
 */
@RestController
@RequestMapping("/bbs/carticle")
public class ColunmArticleController {

    @Resource
    private ColunmArticleService colunmArticleService;

    @Resource
    private LabelService labelService;

    //删除专栏文章
    @PostMapping("deleteArticle")
    public R deleteArticle(HttpServletRequest request , String articleId , String cloumnId){
         String userId = JwtUtils.getMemberIdByJwtToken(request);
         if(userId == null || StringUtils.isEmpty(articleId) || StringUtils.isEmpty(cloumnId)){
             throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
         }
         colunmArticleService.deleteArticle(userId , articleId , cloumnId);
         return R.ok();
    }

    //发布专栏文章
    @PostMapping("publishArticle")
    public R publishArticle(PulishColumnArticleVo pulishColumnArticleVo , //文章基本数据
                            String[] labelList , //标签
                            String conlumnId ,  //专栏id
                            HttpServletRequest request ,
                            String nickname ,
                            String avatar){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(conlumnId) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(avatar)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        Article article = colunmArticleService.publishArticle(pulishColumnArticleVo , conlumnId , userId , nickname , avatar);
        labelService.addArticleLabel(article.getId() , Arrays.asList(labelList));
        return R.ok();
    }
}

