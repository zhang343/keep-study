package com.kuang.course.controller.inside;

import com.kuang.course.service.CmsBillService;
import com.kuang.course.service.CmsCourseService;
import com.kuang.springcloud.entity.MessageCourseVo;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/inside/course")
@Slf4j
public class InsideCourseController {

    @Resource
    private CmsBillService billService;

    @Resource
    private CmsCourseService courseService;

    //查询用户购买课程数量
    @GetMapping("findUserBillNumber")
    public R findUserBillNumber(String userId){
        Integer buyCourseNumber = billService.findUserBillNumber(userId);
        return R.ok().data("buyCourseNumber" , buyCourseNumber);
    }

    //为消息模块服务，查询课程
    @GetMapping("findMessageCourseVo")
    public R findMessageCourseVo(@RequestParam("courseIdList") List<String> courseIdList){
        List<MessageCourseVo> messageCourseVos = courseService.findMessageCourseVo(courseIdList);
        return R.ok().data("messageCourseVos" , messageCourseVos);
    }

}
