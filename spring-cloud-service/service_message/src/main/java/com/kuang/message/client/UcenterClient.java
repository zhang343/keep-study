package com.kuang.message.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/inside/userinfo/findAvatarAndNicknameByUserId")
    R findAvatarAndNicknameByUserId(@RequestParam("userId") String userId);
}
