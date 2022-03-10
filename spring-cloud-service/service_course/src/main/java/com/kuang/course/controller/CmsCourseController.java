package com.kuang.course.controller;


import com.alibaba.fastjson.JSON;
import com.kuang.course.client.VipClient;
import com.kuang.course.entity.vo.CourseVo;
import com.kuang.course.service.CmsBillService;
import com.kuang.course.service.CmsCourseService;
import com.kuang.course.service.CmsVideoService;
import com.kuang.springcloud.entity.BbsCourseVo;
import com.kuang.springcloud.entity.InfoMyNewsVo;
import com.kuang.springcloud.entity.MessageCourseVo;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.rabbitmq.MsgProducer;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private VipClient vipClient;

    @Resource
    private CmsVideoService videoService;

    //查找课程详细信息
    @GetMapping("findCourseDetaile")
    public R findCourseDetaile(String courseId , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(courseId)){
            log.warn("有人进行非法查询课程操作,课程id:" + courseId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        CourseVo courseVo = courseService.findCourseVo(courseId);
        //查账单
        boolean isBuy = false;
        if(courseVo.getPrice() != 0 && userId != null){
            isBuy = billService.findBillByCourseIdAndUserId(courseId , userId);
            if(!isBuy){
                log.info("开始进行远程调用,查出用户对应vip等级的打折数,用户id:" + userId);
                R result = vipClient.findUserCourseDiscount();
                if(result.getSuccess()){
                    double courseDiscount = (double) result.getData().get("courseDiscount");
                    courseVo.setPrice((int) (courseVo.getPrice() * courseDiscount));
                }
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

    //查找价格为前三的课程
    @GetMapping("findCourseOrderByPrice")
    public R findCourseOrderByPrice(){
        log.info("查询课程价格为前三的课程");
        List<BbsCourseVo> bbsCourseVoList = courseService.findCourseOrderByPrice();
        return R.ok().data("courseList" , bbsCourseVoList);
    }


    //查找课程相关信息，为消息模块服务
    @GetMapping("findMessageCourseDetaile")
    public R findMessageCourseDetaile(@RequestParam("courseIdList") List<String> courseIdList){
        if(courseIdList == null || courseIdList.size() == 0 || courseIdList.size() >= 20){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确查询");
        }

        //查询课程小节数量
        Future<Map<String, Integer>> videoNumberByCourseId = videoService.findVideoNumberByCourseId(courseIdList);
        //查询课程
        List<MessageCourseVo> messageCourseVos = courseService.findMessageCourseDetaile(courseIdList);

        Map<String, Integer> map = null;
        //等待一段时间并获取课程小节数量结果
        for(int i = 0 ; i < 5 ; i++){
            if(videoNumberByCourseId.isDone()){
                try {
                    map = videoNumberByCourseId.get();
                }catch(Exception e){
                    log.warn("查询课程小节数量失败");
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

        if(map != null){
            for(MessageCourseVo messageCourseVo : messageCourseVos){
                messageCourseVo.setVideoNumber(map.get(messageCourseVo.getCourseId()));
            }
        }
        return R.ok().data("courseNewsList" , messageCourseVos);

    }

}

