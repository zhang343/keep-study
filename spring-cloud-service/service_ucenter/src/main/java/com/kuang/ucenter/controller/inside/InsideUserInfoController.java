package com.kuang.ucenter.controller.inside;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.UserInfo;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inside/userinfo")
@Slf4j
public class InsideUserInfoController {

    @Resource
    private UserInfoService userInfoService;

    //查询用户头像和昵称
    @GetMapping("findAvatarAndNicknameByUserId")
    public R findAvatarAndNicknameByUserId(String userId){
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "查询用户头像和昵称失败");
        }
        UserInfo byId = userInfoService.getById(userId);
        return R.ok().data("avatar" , byId.getAvatar()).data("nickname" , byId.getNickname());
    }
}
