package com.kuang.course.controller;


import com.kuang.course.entity.vo.CourseVo;
import com.kuang.course.service.CmsBillService;
import com.kuang.course.service.CmsCourseService;
import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/cms/course")
@Slf4j
public class CmsCourseController {

    @Resource
    private CmsCourseService courseService;

    @Resource
    private CmsBillService billService;


    //查找课程详细信息
    @GetMapping("findCourseDetail")
    public R findCourseDetaile(String courseId , HttpServletRequest request){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(courseId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        CourseVo courseVo = courseService.findCourseVo(courseId);

        boolean isBuy = false;
        if(courseVo.getPrice() != 0 && userId != null){
            isBuy = billService.findBillByCourseIdAndUserId(courseId , userId);
            if(!isBuy){
                RightRedis userRightRedis = VipUtils.getUserRightRedis(userId);
                if(userRightRedis != null){
                    double courseDiscount = userRightRedis.getCourseDiscount();
                    courseVo.setPrice((int) (courseVo.getPrice() * courseDiscount));
                }
            }
        }

        long setSize = RedisUtils.getSetSize(courseVo.getId());
        courseVo.setViews(courseVo.getViews() + setSize);
        return R.ok().data("course" , courseVo).data("isBuy" , isBuy);
    }

    //用户购买课程
    @PostMapping("buyCourse")
    public R buyCourse(String courseId , HttpServletRequest request){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(courseId) || userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        //购买课程
        String title = courseService.buyCourse(courseId , userId);
        //用户购买课程成功，发送消息到rabbitmq
        courseService.sendMyNews(userId , courseId , title);
        return R.ok();
    }

}

