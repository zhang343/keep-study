package com.kuang.ucenter.client;

import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
            public R findUserbbsArticleNumber(String userId) {
                return R.error();
            }
        };
    }
}
