package com.kuang.ucenter.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author XiaoZhang
 * @date 2022/2/6 15:32
 * 远程调用service-bbs服务类
 */
@FeignClient(
        name = "service-bbs" ,
        fallbackFactory = BbsClientFactory.class
)
public interface BbsClient {

    @GetMapping("/bbs/article/findUserArticleNumber")
    R findUserArticleNumber();

    //这里手动注入token
    @GetMapping("/bbs/article/findUserReleaseArticleNumber")
    R findUserReleaseArticleNumber(@RequestHeader("token") String token);

    @GetMapping("/bbs/article/findURANAndCN")
    R findURANAndCN(@RequestHeader("token") String token);

}
