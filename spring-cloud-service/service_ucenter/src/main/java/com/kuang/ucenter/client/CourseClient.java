package com.kuang.ucenter.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(
        name = "service-course" ,
        fallbackFactory = CourseClientFactory.class
)
public interface CourseClient {

    //查询用户学习数量
    @GetMapping("/inside/study/findUserStudyNumber")
    R findUserStudyNumber(@RequestParam("userId") String userId);
}
