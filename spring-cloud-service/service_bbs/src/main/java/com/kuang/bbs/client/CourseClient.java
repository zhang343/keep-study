package com.kuang.bbs.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用service-course服务类
 */
@FeignClient(
        name = "service-course" ,
        fallbackFactory = CourseClientFactory.class
)
public interface CourseClient {
    @GetMapping("/cms/course/findCourseOrderByPrice")
    R findCourseOrderByPrice();
}
