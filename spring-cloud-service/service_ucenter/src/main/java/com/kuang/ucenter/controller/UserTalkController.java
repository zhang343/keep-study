package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.vo.OtherUserTalkVo;
import com.kuang.ucenter.entity.vo.UserTalkVo;
import com.kuang.ucenter.service.UserTalkService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/user/talk")
public class UserTalkController {

    @Resource
    private UserTalkService talkService;

    //用户发表说说
    @PostMapping("publishTalk")
    public R publishTalk(HttpServletRequest request , String content){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(content)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        UserTalkVo userTalkVo = talkService.publishTalk(userId , content);
        return R.ok().data("talk" , userTalkVo);
    }

    //查找用户说说
    @GetMapping("findUserTalk")
    public R findUserTalk(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                          @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                          HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        Integer total = talkService.findUserTalkNumber(userId);
        List<UserTalkVo> userTalkVoList = talkService.findUserTalk(userId , current , limit);
        return R.ok().data("total" , total).data("talkList" , userTalkVoList);
    }

    //删除用户说说
    @PostMapping("deleteUserTalk")
    public R deleteUserTalk(HttpServletRequest request , String id){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(id)){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        talkService.deleteUserTalk(userId , id);
        return R.ok();
    }

    //修改用户说说是否可以公开
    @PostMapping("updateUserTalkIsPublic")
    public R updateUserTalkIsPublic(HttpServletRequest request , String id , Boolean isPublic){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(id) || isPublic == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        talkService.updateUserTalkIsPublic(userId , id , isPublic);
        return R.ok();
    }


    //查询他人用户说说
    @GetMapping("findOtherUserTalk")
    public R findOtherUserTalk(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                               @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                               String userId){
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        Integer total = talkService.findOtherUserTalkNumber(userId);
        List<OtherUserTalkVo> otherUserTalkVos = talkService.findOtherUserTalk(userId , current , limit);
        return R.ok().data("total" , total).data("otherUserTalkList" , otherUserTalkVos);
    }
}

