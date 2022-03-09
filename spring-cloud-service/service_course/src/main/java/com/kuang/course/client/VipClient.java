package com.kuang.course.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author XiaoZhang
 * @date 2022/2/10 15:32
 * 远程调用service-vip服务类
 */
@FeignClient(
        name = "service-vip" ,
        fallbackFactory = VipClientFactory.class
)
public interface VipClient {

    @GetMapping("/vm/trai/findUserCourseDiscount")
    R findUserCourseDiscount();
}
