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
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询课程通知消息,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        //查询出课程id
        List<String> courseIdList = courseService.findUserNewsId(current, limit, userId);
        Future<Object> messageCourseVos = courseService.findMessageCourseVos(courseIdList);
        Integer total = courseService.findUserNewsNumber(userId);
        courseService.setCourseRead(courseIdList , userId);

        Object courseNewsList = null;
        for(int i = 0 ; i < 5 ; i++){
            if(messageCourseVos.isDone()){
                try {
                    courseNewsList = messageCourseVos.get();
                }catch(Exception e){
                    log.warn("远程调用查询课程失败");
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch(InterruptedException e) {
                log.warn("休眠失败");
            }
        }
        return R.ok().data("total" , total).data("courseNewsList" , courseNewsList);
    }
}

