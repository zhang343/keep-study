package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.vo.UserFollowOrFans;
import com.kuang.ucenter.service.UserAttentionService;
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
 * @since 2022-02-05
 */
@RestController
@RequestMapping("/user/attention")
@Slf4j
public class UserAttentionController {

    @Resource
    private UserAttentionService userAttentionService;


    //查询关注用户
    @GetMapping("findUserFollow")
    public R findUserFollow(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                            @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                            HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        Integer total = userAttentionService.findUserFollowNumber(userId);
        List<UserFollowOrFans> userFollowOrFansList = userAttentionService.findUserFollow(userId , current , limit);
        return R.ok().data("total" , total).data("userFollowList" , userFollowOrFansList);
    }

    //查询粉丝
    @GetMapping("findUserFans")
    public R findUserFans(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                          @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                          HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        Integer total = userAttentionService.findUserFansNumber(userId);
        List<UserFollowOrFans> userFollowOrFansList = userAttentionService.findUserFans(userId , current , limit);
        return R.ok().data("total" , total).data("userFansList" , userFollowOrFansList);
    }


}

