package com.kuang.bbs.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 远程调用service-vip服务类
 */
@FeignClient(
        name = "service-vip" ,
        fallbackFactory = VipClientFactory.class
)
public interface VipClient {
    @GetMapping("/inside/member/findUserVipLevelByUserIdList")
    R findUserVipLevelByUserIdList(@RequestParam("userIdList") List<String> userIdList);

    @GetMapping("/inside/right/findRightRedisByUserId")
    R findRightRedisByUserId(@RequestParam("userId") String userId);
}
