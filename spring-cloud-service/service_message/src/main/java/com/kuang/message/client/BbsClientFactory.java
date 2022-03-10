package com.kuang.message.client;

import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author XiaoZhang
 * @date 2022/2/6 11:35
 * 远程调用service-bbs服务熔断降级类
 */
@Component
@Slf4j
public class BbsClientFactory implements FallbackFactory<BbsClient> {

    @Override
    public BbsClient create(Throwable throwable) {
        return new BbsClient() {
            @Override
            public R findUserArticleNumber() {
                log.error("远程调用service-bbs下面的/bbs/article/findUserArticleNumber接口失败");
                return R.error();
            }

            @Override
            public R findUserReleaseArticleNumber(String token) {
                log.error("远程调用service-bbs下面的/bbs/article/findUserReleaseArticleNumber接口失败");
                return R.error();
            }

            @Override
            public R findURANAndCN(String token) {
                log.error("远程调用service-bbs下面的/bbs/article/findURANAndCN接口失败");
                return R.error();
            }

            @Override
            public R findArticleViews(List<String> articleList) {
                log.error("远程调用service-bbs下面的/bbs/article/findArticleViews接口失败");
                return R.error();
            }

        };
    }
}
