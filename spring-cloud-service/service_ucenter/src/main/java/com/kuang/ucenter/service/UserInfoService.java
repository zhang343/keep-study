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
    UserInfo insertMember(String openid , String nickname , String headimgurl);

    //给用户增加k币
    void addKCoin(Integer kCoinNumber, String userId);

    //查询系统用户数量
    int findUserNumber();

    //用户账号密码登录
    String login(String loginAct, String loginPwd);

    //用户登录之后查询小方框内容
    MyUserInfoVo findUserSmallBoxContent(String userId);

    //用户签到
    int userSignIn(String userId);

    //更新用户每日签到
    void updateUserIsSign();

    //修改用户背景图像
    void setUserBgimg(String userId, String url);

    //查询出用户资料
    UserDateVo findUserData(String userId);

    //修改用户资料
    void setUserData(String userId, UserSetDataVo userSetDataVo);

    //查询用户安全信息
    UserSecurity findUserSecurityData(String userId);

    //修改用户安全信息
    void setUserSecurityData(String userId, String email, String password);

    //根据条件查找用户数量
    Integer findUserByAccountOrNicknameNumber(String accountOrNickname);

    //根据条件查找用户
    List<UserSearchVo> findUserByAccountOrNickname(String accountOrNickname , Long current , Long limit);

    //查询用户主页的内容,这里查自己
    UserDetailVo findUserHomePage(String userId);


    //查询其他用户主页内容
    OtherUserDetailVo findOtherUserHomePage(String userId);

    //查询右下边框内容
    UserLowerRightBox findLowerRightBox(String userId);


    //发送我的注册消息
    void setMyRegisterNews(String id);
}
