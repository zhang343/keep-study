package com.kuang.bbs.controller;

import com.kuang.bbs.entity.vo.ArticleCacheVo;
import com.kuang.bbs.service.ArticleService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 * 文章缓存处理类
 */
@RestController
@RequestMapping("/bbs/cache")
@Slf4j
public class CacheController {


    @Resource
    private ArticleService articleService;

    //文章缓存,时间是30分钟
    @PostMapping("setArticleCache")
    public R setArticleCache(ArticleCacheVo articleCacheVo , List<String> labelList , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("设置文章的缓存,用户id:" + userId);
        if(userId == null || StringUtils.isEmpty(articleCacheVo.getContent())){
            return R.ok();
        }
        articleService.setArticleCache(articleCacheVo , labelList , userId);
        return R.ok();
    }


    //查询文章缓存
    @GetMapping("findArticleCache")
    public R findArticleCache(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询文章缓存,用户id:" + userId);
        if(userId == null){
            log.warn("有人非法查询文章缓存,用户id:" + null);
            throw new XiaoXiaException(ResultCode.ERROR , "查询文章缓存失败");
        }
        ArticleCacheVo articleCacheVo = articleService.findArticleCache(userId);
        return R.ok().data("article" , articleCacheVo);
    }


}
