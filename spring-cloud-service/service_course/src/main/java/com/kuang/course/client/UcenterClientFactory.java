package com.kuang.course.client;


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
                log.error("远程调用service-ucenter下面的/KCoin/reduce方法失败");
                return R.error();
            }
        };
    }
}
