package com.kuang.vip.client;


import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author XiaoZhang
 * @date 2022/2/6 11:35
 * 远程调用service-ucenter服务熔断降级类
 */
@Component
@Slf4j
public class UcenterClientFactory implements FallbackFactory<UcenterClient> {

    @Override
    public UcenterClient create(Throwable throwable) {
        return new UcenterClient() {

            @Override
            public R add(Integer kCoinNumber) {
                log.error("远程调用service-ucenter下面的/KCoin/add方法失败");
                return R.error();
            }
        };
    }
}
