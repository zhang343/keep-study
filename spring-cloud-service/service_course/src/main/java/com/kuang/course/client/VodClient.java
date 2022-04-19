package com.kuang.course.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(
        name = "service-vod" ,
        fallbackFactory = VodClientFactory.class
)
public interface VodClient {

    @GetMapping("/inside/video/getPlayAuth")
    R getPlayAuth(@RequestParam("videoSourceId") String videoSourceId);

    @PostMapping("deleteVideo")
    R deleteVideo(@RequestParam("videoSourceId") String videoSourceId , @RequestHeader("token") String token);
}
