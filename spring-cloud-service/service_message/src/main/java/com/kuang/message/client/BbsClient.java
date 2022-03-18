package com.kuang.message.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    @GetMapping("/inside/article/findArticleViews")
    R findArticleViews(@RequestParam("articleIdList") List<String> articleIdList);

}
