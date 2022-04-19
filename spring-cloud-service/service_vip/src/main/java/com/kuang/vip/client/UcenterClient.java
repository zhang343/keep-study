package com.kuang.vip.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@FeignClient(
        name = "service-ucenter" ,
        fallbackFactory = UcenterClientFactory.class
)
public interface UcenterClient {

    @PostMapping("/KCoin/add")
    R add(@RequestParam("kCoinNumber") Integer kCoinNumber , @RequestParam("userId") String userId);

    @PostMapping("/KCoin/reduce")
    R reduce(@RequestParam("kCoinNumber") Integer kCoinNumber , @RequestParam("userId") String userId);
}
