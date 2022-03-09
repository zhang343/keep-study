package com.kuang.vod.client;


import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author XiaoZhang
 * @date 2022/2/6 11:35
 * 远程调用service-course服务熔断降级类
 */
@Component
@Slf4j
public class CourseClientFactory implements FallbackFactory<CourseClient> {

    @Override
    public CourseClient create(Throwable throwable) {
        return new CourseClient() {
            @Override
            public R findUserAbility(String id) {
                log.error("远程调用service-course下面的/cms/video/findUserAbility接口失败");
                return R.error();
            }
        };
    }
}
