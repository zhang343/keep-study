package com.kuang.ucenter.controller;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.vo.UserSearchVo;
import com.kuang.ucenter.entity.vo.UserVo;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

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

    //全站查找用户,未被封号的
    @GetMapping("findUser")
    public R findUser(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                      @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                      String accountOrNickname){
        log.info("开始查询用户,用户账号或者昵称为:" + accountOrNickname);
        if(StringUtils.isEmpty(accountOrNickname)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        List<UserSearchVo> userVoList = userInfoService.findUserByCondition(current , limit , accountOrNickname);
        Long total = userInfoService.findUserNumberByCondition(accountOrNickname);
        return R.ok().data("total" , total).data("userList" , userVoList);
    }
}
