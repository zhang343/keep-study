package com.kuang.ucenter.client;

import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class VipClientFactory implements FallbackFactory<VipClient> {

    @Override
    public VipClient create(Throwable throwable) {
        return new VipClient() {
            @Override
            public R findRightRedisByUserId(String userId) {
                return R.error();
            }
        };
    }
}
