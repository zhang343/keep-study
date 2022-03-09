package com.kuang.vip.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author XiaoZhang
 * @date 2022/2/6 15:32
 * 远程调用service-ucenter服务类
 */
@FeignClient(
        name = "service-ucenter" ,
        fallbackFactory = UcenterClientFactory.class
)
public interface UcenterClient {

    @PostMapping("/KCoin/reduce")
    R reduce(@RequestParam("kCoinNumber") Integer kCoinNumber);

    @PostMapping("/KCoin/add")
    R add(@RequestParam("kCoinNumber") Integer kCoinNumber);
}
