package com.kuang.bbs.controller;


import com.kuang.bbs.entity.vo.CollectArticleVo;
import com.kuang.bbs.service.CollectService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-23
 */
@RestController
@RequestMapping("/bbs/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    //增加用户收藏文章
    @PostMapping("addUserCollectArticle")
    public R addUserCollectArticle(HttpServletRequest request ,
                                   String articleId){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(articleId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        collectService.addUserCollectArticle(userId , articleId);
        return R.ok();
    }

    //删除用户收藏文章
    @PostMapping("deleteUserCollectArticle")
    public R deleteUserCollectArticle(HttpServletRequest request ,
                                      String articleId){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(articleId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        collectService.deleteUserCollectArticle(userId , articleId);
        return R.ok();
    }


    //查询用户收藏文章
    @GetMapping("findUserCollectArticle")
    public R findUserCollectArticle(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                                    @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                                    HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        Integer total = collectService.findUserCollectArticleNumber(userId);
        List<CollectArticleVo> collectArticleVoList = collectService.findUserCollectArticle(userId , current , limit);
        return R.ok().data("total" , total).data("collectArticleList" , collectArticleVoList);
    }
}

