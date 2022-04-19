package com.kuang.message.client;

import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class BbsClientFactory implements FallbackFactory<BbsClient> {

    @Override
    public BbsClient create(Throwable throwable) {
        return new BbsClient() {

            @Override
            public R findArticleViews(List<String> articleIdList) {
                return R.error();
            }
        };
    }
}
