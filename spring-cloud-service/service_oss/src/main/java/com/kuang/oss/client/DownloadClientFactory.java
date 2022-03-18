package com.kuang.oss.client;


import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author XiaoZhang
 * @date 2022/2/6 11:35
 * 远程调用service-download服务熔断降级类
 */
@Component
@Slf4j
public class DownloadClientFactory implements FallbackFactory<DownloadClient> {

    @Override
    public DownloadClient create(Throwable throwable) {
        return new DownloadClient() {
            @Override
            public R findFileNameAndPriceById(String id) {
                return R.error();
            }
        };
    }
}
