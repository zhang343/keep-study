package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.vo.CollectArticleVo;
import com.kuang.ucenter.service.UserCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@RestController
@RequestMapping("/user/collection")
@Slf4j
public class UserCollectController {

    @Resource
    private UserCollectService userCollectService;

    //查询用户是否收藏了某个文章
    @GetMapping("findUserIsCollection")
    public R findUserIsCollection(String articleId , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询用户是否收藏了文章,文章id:" + articleId + ",用户id:" + userId);
        boolean isCollection = false;
        if(StringUtils.isEmpty(articleId) || userId == null){
            return R.ok().data("isCollection" , false);
        }else {
            isCollection = userCollectService.findUserIsCollection(articleId , userId);
        }
        return R.ok().data("isCollection" , isCollection);
    }

    //用户收藏文章
    @PostMapping("addCollectionArticle")
    public R addCollectionArticle(String articleId , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户收藏文章,文章id:" + articleId + ",用户id:" + userId);
        if(StringUtils.isEmpty(articleId) || userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        userCollectService.addCollectionArticle(articleId , userId);
        return R.ok();
    }

    //查询我的收藏文章数量
    @GetMapping("findMyCollectionArticleNumber")
    public R findMyCollectionArticleNumber(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户收藏文章数量,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Integer collectionNumber = userCollectService.findUserCollectionNumber(userId);
        return R.ok().data("collectionNumber" , collectionNumber);
    }


    //查询用户收藏文章
    @GetMapping("findMyCollectionArticle")
    public R findMyCollectionArticle(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                                     @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                                     HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询用户收藏文章,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Future<List<CollectArticleVo>> userCollection = userCollectService.findUserCollection(current, limit, userId);
        Integer collectionNumber = userCollectService.findUserCollectionNumber(userId);
        List<CollectArticleVo> articleList = null;
        for(int i = 0 ; i < 10 ; i++){
            if(userCollection.isDone()){
                try {
                    articleList = userCollection.get();
                } catch(Exception e) {
                    log.error("查询用户收藏失败");
                    throw new XiaoXiaException(ResultCode.ERROR , "查询失败");
                }
                break;
            }
            //如果没有执行完毕，则最多等待0.2秒
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch(InterruptedException e) {
                log.warn("休眠失败");
            }

        }
        return R.ok().data("total" , collectionNumber).data("articleList" , articleList);
    }
}

