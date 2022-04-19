package com.kuang.oss.client;


import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



@Component
@Slf4j
public class UcenterClientFactory implements FallbackFactory<UcenterClient> {

    @Override
    public UcenterClient create(Throwable throwable) {
        return new UcenterClient() {
            @Override
            public R reduce(Integer kCoinNumber , String userId) {
                return R.error();
            }
        };
    }
}
