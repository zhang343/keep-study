package com.kuang.message.controller;


import com.kuang.message.service.InfoCourseService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@RestController
@RequestMapping("/message/course")
@Slf4j
public class InfoCourseController {

    @Resource
    private InfoCourseService courseService;

    //查询课程通知
    @PostMapping("findAll")
    public R findAll(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                     @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                     HttpServletRequest request){
        System.out.println("----" + LocalTime.now() + "----");
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询课程通知消息,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        //查询出课程id
        List<String> courseIdList = courseService.findUserNewsId(current, limit, userId);
        //异步远程调用查出课程详细信息
        Future<Object> userNews = courseService.findUserNews(courseIdList);
        Integer total = courseService.findUserNewsNumber(userId);
        courseService.setCourseRead(courseIdList , userId);

        Object messageCourseVos = null;
        for(int i = 0 ; i < 10 ; i++){
            if(userNews.isDone()){
                try {
                    messageCourseVos = userNews.get();
                }catch(Exception e){
                    log.warn("查询课程通知失败");
                    throw new XiaoXiaException(ResultCode.ERROR , "查询课程通知失败");
                }
                break;
            }
            //如果没有执行完毕，则最多等待0.2秒
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch(InterruptedException e) {
                log.warn("休眠失败");
            }
        }

        System.out.println("----" + LocalTime.now() + "----");
        return R.ok().data("total" , total).data("courseNewsList" , messageCourseVos);
    }
}

