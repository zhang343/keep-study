package com.kuang.message.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author XiaoZhang
 * @date 2022/2/6 15:32
 * 远程调用service-vip服务类
 */
@FeignClient(
        name = "service-vip" ,
        fallbackFactory = VipClientFactory.class
)
public interface VipClient {

    @GetMapping("/vm/user/findMemberRightLogo")
    R findMemberRightLogo(@RequestParam("userIdList") List<String> userIdList);

    @GetMapping("/vm/user/findMemberRightVipLevel")
    R findMemberRightVipLevel(@RequestParam("userId") String userId);

    @PostMapping("/usertodayright/addArticle")
    R addArticle();

}
