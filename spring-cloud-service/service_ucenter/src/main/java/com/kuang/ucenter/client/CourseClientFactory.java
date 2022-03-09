package com.kuang.ucenter.client;


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
            public R findUserBillNumber(String userId) {
                log.error("远程调用service-course下面的/cms/bill/findUserBillNumber接口失败");
                return R.error();
            }
        };
    }
}
