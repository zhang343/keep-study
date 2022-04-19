package com.kuang.ucenter.client;

import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


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

            @Override
            public R findUserAllArticleNumber(String userId) {
                return R.error();
            }

            @Override
            public R findUserCommentNumber(String userId) {
                return R.error();
            }

            @Override
            public R findUserColumnNumber(String userId) {
                return R.error();
            }

            @Override
            public R findOtherUserColumnNumber(String userId) {
                return R.error();
            }

            @Override
            public R addArticleRight(String userId) {
                return R.error();
            }
        };
    }
}
