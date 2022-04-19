package com.kuang.ucenter.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(
        name = "service-bbs" ,
        fallbackFactory = BbsClientFactory.class
)
public interface BbsClient {

    @GetMapping("/inside/article/findUserbbsArticleNumber")
    R findUserbbsArticleNumber(@RequestParam("userId") String userId);

    //查询用户所有江湖文章数量
    @GetMapping("/inside/article/findUserAllArticleNumber")
    R findUserAllArticleNumber(@RequestParam("userId") String userId);

    //查询用户所有评论数量
    @GetMapping("/inside/comment/findUserCommentNumber")
    R findUserCommentNumber(@RequestParam("userId") String userId);

    //查看用户专栏数量
    @GetMapping("/inside/column/findUserColumnNumber")
    R findUserColumnNumber(@RequestParam("userId") String userId);

    //查看其他用户专栏数量
    @GetMapping("/inside/column/findOtherUserColumnNumber")
    R findOtherUserColumnNumber(@RequestParam("userId") String userId);

    //插入文章权益，用户模块创建用户会用到
    @PostMapping("/inside/aright/addArticleRight")
    R addArticleRight(@RequestParam("userId") String userId);
}
