package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.ucenter.entity.vo.*;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserInfoService extends IService<UserInfo> {

    //根据微信id查询用户
    UserInfo getOpenIdMember(String openid);

    //创建一个用户
    void insertMember(UserInfo member);

    //给用户增加k币
    void addKCoin(Integer kCoinNumber, String userId);

    //查询系统用户数量
    int findUserNumber();

    //用户账号密码登录
    String login(String loginAct, String loginPwd);
}
