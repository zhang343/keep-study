package com.kuang.ucenter.controller;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.vo.UserSearchVo;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@RestController
@RequestMapping("/user/search")
@Slf4j
public class UserSearchController {

    @Resource
    private UserInfoService userInfoService;

    //全站查找用户
    @GetMapping("findUserByAccountOrNickname")
    public R findUserByAccountOrNickname(String accountOrNickname){
        if(StringUtils.isEmpty(accountOrNickname)) {
            throw new XiaoXiaException(ResultCode.ERROR, "请输入");
        }
        Integer total = userInfoService.findUserByAccountOrNicknameNumber(accountOrNickname);
        List<UserSearchVo> userSearchVoList = userInfoService.findUserByAccountOrNickname(accountOrNickname);
        return R.ok().data("total" , total).data("userList" , userSearchVoList);
    }

}
