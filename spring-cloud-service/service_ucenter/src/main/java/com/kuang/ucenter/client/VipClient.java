package com.kuang.ucenter.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @GetMapping("/vm/user/findMemberRightVipLevelAndIsSign")
    R findMemberRightVipLevelAndIsSign(@RequestHeader("token") String token);

    @GetMapping("/vm/user/findSignExperience")
    R findSignExperience();

    //用户签到接口权益
    @PostMapping("/usertodayright/toSign")
    R toSign();

}
