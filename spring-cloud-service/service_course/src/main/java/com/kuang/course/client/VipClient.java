package com.kuang.course.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(
        name = "service-vip" ,
        fallbackFactory = VipClientFactory.class
)
public interface VipClient {


    @GetMapping("/inside/right/findRightRedisByUserId")
    R findRightRedisByUserId(@RequestParam("userId") String userId);
}
