package com.kuang.course.controller;


import com.alibaba.fastjson.JSON;
import com.kuang.course.entity.vo.CourseVo;
import com.kuang.course.service.CmsBillService;
import com.kuang.course.service.CmsCourseService;
import com.kuang.springcloud.entity.InfoMyNewsVo;
import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.rabbitmq.MsgProducer;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 * 课程处理类
 */
@RestController
@RequestMapping("/cms/course")
@Slf4j
public class CmsCourseController {

    @Resource
    private CmsCourseService courseService;

    @Resource
    private CmsBillService billService;

    @Resource
    private MsgProducer msgProducer;

    //查找课程详细信息
    @GetMapping("findCourseDetaile")
    public R findCourseDetaile(String courseId , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(courseId)){
            log.warn("有人进行非法查询课程操作,课程id:" + courseId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        CourseVo courseVo = courseService.findCourseVo(courseId);

        boolean isBuy = false;
        if(courseVo.getPrice() != 0 && userId != null){
            isBuy = billService.findBillByCourseIdAndUserId(courseId , userId);
        }

        if(!isBuy){
            RightRedis userRightRedis = VipUtils.getUserRightRedis(userId);
            if(userRightRedis != null){
                double courseDiscount = userRightRedis.getCourseDiscount();
                courseVo.setPrice((int) (courseVo.getPrice() * courseDiscount));
            }
        }
        return R.ok().data("course" , courseVo).data("isBuy" , isBuy);
    }

    //用户购买课程
    @PostMapping("buyCourse")
    public R buyCourse(String courseId , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("有用户购买课程,课程id:" + courseId + ",用户id:" + userId);
        if(StringUtils.isEmpty(courseId) || userId == null){
            log.warn("有用户非法购买课程课程id:" + courseId + ",用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        String title = courseService.buyCourse(courseId , userId);
        log.info("用户购买课程,发送消息到我的消息,课程id:" + courseId + ",用户id:" + userId);
        try {
            InfoMyNewsVo infoMyNewsVo = new InfoMyNewsVo();
            infoMyNewsVo.setUserId(userId);
            infoMyNewsVo.setIsCourse(true);
            infoMyNewsVo.setCourseTitle(title);
            infoMyNewsVo.setCourseId(courseId);
            msgProducer.sendMyNews(JSON.toJSONString(infoMyNewsVo));
        }catch(Exception e){
            log.warn("用户购买课程,发送消息到我的消息失败,课程id:" + courseId + ",用户id:" + userId);
        }
        return R.ok();
    }

}

