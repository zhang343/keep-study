package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.vo.UserVo;
import com.kuang.ucenter.service.UserAttentionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Future;

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

    //查询出用户的所有粉丝id
    @GetMapping("findUserFansId")
    public R findUserFansId(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询出指定用户的所有粉丝id,用户id:" + userId);
        if(userId == null){
            log.info("有人非法查询指定用户的所有粉丝id,用户id:" + null);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        List<String> userIdList = userAttentionService.findUserFansId(userId);
        return R.ok().data("userIdList" , userIdList);
    }

    //查询用户粉丝或者关注
    @GetMapping("findAOrF")
    public R findAOrF(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                      @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                      String userId ,
                      Integer isAttention){
        log.info("开始查询用户粉丝或者关注,用户id:" + userId + ",标志位:" + isAttention);
        if(StringUtils.isEmpty(userId) || (isAttention != 1 && isAttention != 0)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        Future<List<UserVo>> userVo = null;
        Integer total = 0;
        if(isAttention == 0){
            //查询关注
            userVo = userAttentionService.findFocusOnUser(userId , current , limit);
            total = userAttentionService.findUserAttentionNumber(userId);
        }else {
            //查询粉丝
            userVo = userAttentionService.findFansUser(userId , current , limit);
            total = userAttentionService.findUserFansNumber(userId);
        }

        List<UserVo> userVoList = null;
        try {
            userVoList = userVo.get();
        } catch(Exception e) {
            log.error("根据条件查询用户失败");
            throw new XiaoXiaException(ResultCode.ERROR , "查询用户失败");
        }
        return R.ok().data("userList" , userVoList).data("total" , total);
    }

    //增加用户关注
    @PostMapping("addUserAttention")
    public R addUserAttention(String userId , HttpServletRequest request){
        String myUserId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户" + myUserId + "去关注用户:" + userId);
        if(StringUtils.isEmpty(userId) || myUserId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        userAttentionService.addUserAttention(myUserId , userId);
        return R.ok();
    }
}

