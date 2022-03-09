package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.service.UserCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

}

