package com.kuang.bbs.controller;

import com.kuang.bbs.entity.vo.ArticleCacheVo;
import com.kuang.bbs.service.CacheArticleService;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bbs/cache")
@Slf4j
public class CacheController {


    @Resource
    private CacheArticleService cacheArticleService;


    //文章缓存,时间是30分钟
    @PostMapping("setArticleCache")
    public R setArticleCache(ArticleCacheVo articleCacheVo , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId != null){
            cacheArticleService.setArticleCache(articleCacheVo , userId);
        }
        return R.ok();
    }


    //查询文章缓存
    @GetMapping("findArticleCache")
    public R findArticleCache(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        ArticleCacheVo articleCacheVo = cacheArticleService.findArticleCache(userId);
        return R.ok().data("article" , articleCacheVo);
    }


}
