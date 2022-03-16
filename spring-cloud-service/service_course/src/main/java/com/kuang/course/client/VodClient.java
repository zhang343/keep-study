package com.kuang.course.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author XiaoZhang
 * @date 2022/2/6 15:32
 * 远程调用service-vod服务类
 */
@FeignClient(
        name = "service-vod" ,
        fallbackFactory = VodClientFactory.class
)
public interface VodClient {

    @GetMapping("/inside/video/getPlayAuth")
    R getPlayAuth(@RequestParam("videoSourceId") String videoSourceId);
}
