package com.kuang.ucenter.client;

import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author XiaoZhang
 * @date 2022/2/6 11:35
 * 远程调用service-vip服务熔断降级类
 */
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
