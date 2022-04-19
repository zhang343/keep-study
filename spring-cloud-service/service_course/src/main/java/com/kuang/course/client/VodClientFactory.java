package com.kuang.course.client;

import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class VodClientFactory implements FallbackFactory<VodClient> {

    @Override
    public VodClient create(Throwable throwable) {
        return new VodClient() {
            @Override
            public R getPlayAuth(String videoSourceId) {
                return R.error();
            }

            @Override
            public R deleteVideo(String videoSourceId, String token) {
                return R.error();
            }
        };
    }
}
