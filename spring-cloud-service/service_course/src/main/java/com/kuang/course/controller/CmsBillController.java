package com.kuang.course.controller;


import com.kuang.course.entity.vo.CourseStudyVo;
import com.kuang.course.service.CmsBillService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
@RestController
@RequestMapping("/cms/bill")
@Slf4j
public class CmsBillController {

    @Resource
    private CmsBillService billService;

    //查询用户购买课程
    @GetMapping("findUserBuyCourse")
    public R findUserBuyCourse(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                               @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                               HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        Integer total = billService.findUserBillNumber(userId);
        List<CourseStudyVo> courseStudyVoList = billService.findUserBuyCourse(userId , current , limit);
        return R.ok().data("total" , total).data("buyCourseList" , courseStudyVoList);
    }

}

