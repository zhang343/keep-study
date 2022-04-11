package com.kuang.bbs.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "service-ucenter" ,
        fallbackFactory = UcenterClientFactory.class
)
public interface UcenterClient {

    @GetMapping("/inside/userinfo/findAvatarAndNicknameByUserId")
    R findAvatarAndNicknameByUserId(@RequestParam("userId") String userId);

    @GetMapping("/inside/attention/findUserFansId")
    R findUserFansId(@RequestParam("userId") String userId);

    @PostMapping("/KCoin/add")
    R add(@RequestParam("kCoinNumber") Integer kCoinNumber);

}
