package com.kuang.course.client;

import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author XiaoZhang
 * @date 2022/2/10 15:33
 * 远程调用service-vod服务熔断降级类
 */
@Component
@Slf4j
public class VodClientFactory implements FallbackFactory<VodClient> {

    @Override
    public VodClient create(Throwable throwable) {
        return new VodClient() {
            @Override
            public R getPlayAuth(String videoSourceId) {
                log.warn("远程调用service-vod接口/video/getPlayAuth失败");
                return R.error();
            }
        };
    }
}
