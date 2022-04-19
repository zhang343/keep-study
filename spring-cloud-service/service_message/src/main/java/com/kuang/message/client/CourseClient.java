package com.kuang.message.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(
        name = "service-course" ,
        fallbackFactory = CourseClientFactory.class
)
public interface CourseClient {

    @GetMapping("/inside/course/findMessageCourseVo")
    R findMessageCourseVo(@RequestParam("courseIdList") List<String> courseIdList);

}
