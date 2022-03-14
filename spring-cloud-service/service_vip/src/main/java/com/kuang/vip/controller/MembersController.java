package com.kuang.vip.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vip.entity.Rights;
import com.kuang.vip.service.MembersService;
import com.kuang.vip.service.RightsService;
import com.kuang.vip.service.UserTodayRightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Xiaozhang
 * @since 2022-02-07
 * vip用户处理类
 */
@RestController
@RequestMapping("/vm/user")
@Slf4j
public class MembersController {

    @Resource
    private MembersService membersService;

    @Resource
    private UserTodayRightService userTodayRightService;

    @Resource
    private RightsService rightsService;

    //用户充值vip
    @PostMapping("addMember")
    public R addMember(String id , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户充值vip,充值的vip的id:" + id);
        if(StringUtils.isEmpty(id) || userId == null){
            log.warn("有用户非法充值vip操作,vip的id：" + id + ",用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        membersService.addMember(id , userId);
        return R.ok();
    }

    //查询用户的viplogo
    @GetMapping("findMemberRightLogo")
    public R findMemberRightLogo(@RequestParam("userIdList") List<String> userIdList){
        log.info("查询用户的viplogo,用户id:" + userIdList);
        if(userIdList == null || userIdList.size() == 0){
            throw new XiaoXiaException(ResultCode.ERROR , "请传递正确参数");
        }
        Map<String , Object> logo = membersService.findMemberRightLogo(userIdList);
        return R.ok().data(logo);
    }

    //查询用户的vip标识
    @GetMapping("findMemberRightVipLevel")
    public R findMemberRightVipLevel(String userId){
        log.info("查询用户的vip标识,用户id:" + userId);
        if(StringUtils.isEmpty(userId)){
            log.warn("有用户非法查询用户vipLevel,用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        String vipLevel = membersService.findMemberRightVipLevel(userId);
        return R.ok().data("vipLevel" , vipLevel);
    }

    //查询用户的vip标识和是否签到
    @GetMapping("findMemberRightVipLevelAndIsSign")
    public R findMemberRightVipLevelAndIsSign(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询用户的vip标识和是否签到,用户id:" + userId);
        if(userId == null){
            log.warn("有用户的vip标识和是否签到,用户id:" + null);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        String vipLevel = membersService.findMemberRightVipLevel(userId);
        Boolean isSign = userTodayRightService.findIsSign(userId);
        return R.ok().data("vipLevel" , vipLevel).data("isSign" , isSign);
    }

    //查询用户每日签到经验
    @GetMapping("findSignExperience")
    public R findSignExperience(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查看用户每日签到经验,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        Rights rightByUserId = rightsService.findRightByUserId(userId);
        if(rightByUserId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        return R.ok().data("signExperience" , rightByUserId.getSignExperience());
    }

}

