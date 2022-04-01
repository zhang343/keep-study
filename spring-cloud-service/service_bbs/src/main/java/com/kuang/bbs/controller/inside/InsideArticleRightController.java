package com.kuang.bbs.controller.inside;

import com.kuang.bbs.service.ArticleRightService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inside/aright")
@Slf4j
public class InsideArticleRightController {

    @Resource
    private ArticleRightService articleRightService;

    //插入文章权益，用户模块创建用户会用到
    @PostMapping("addArticleRight")
    public R addArticleRight(String userId){
        articleRightService.addArticleRight(userId);
        return R.ok();
    }
}
