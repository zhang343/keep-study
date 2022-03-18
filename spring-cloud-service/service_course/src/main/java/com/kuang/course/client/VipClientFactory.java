package com.kuang.course.client;

import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author XiaoZhang
 * @date 2022/2/10 15:33
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
