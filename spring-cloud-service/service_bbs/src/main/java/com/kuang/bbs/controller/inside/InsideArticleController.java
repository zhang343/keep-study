package com.kuang.bbs.controller.inside;

import com.kuang.bbs.service.ArticleService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inside/article")
@Slf4j
public class InsideArticleController {

    @Resource
    private ArticleService articleService;

    //为消息模块服务，查询文章浏览量
    @GetMapping("findArticleViews")
    public R findArticleViews(@RequestParam("articleIdList") List<String> articleIdList){
        Map<String , Object> articleViews = articleService.findArticleViews(articleIdList);
        return R.ok().data(articleViews);
    }

    //为用户模块服务，查询用户文章在江湖可以查找到的文章数量
    @GetMapping("findUserbbsArticleNumber")
    public R findUserbbsArticleNumber(String userId){
        Integer articleNumber = articleService.findUserbbsArticleNumber(userId);
        return R.ok().data("articleNumber" , articleNumber);
    }
}
