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

    //查询用户头像和昵称
    UserInfo findAvatarAndNicknameByUserId(String userId);

    //查找用户数量，通过条件
    Future<Long> findUserNumberByCondition(String accountOrNickname);

    //查找用户，通过条件
    List<UserVo> findUserByCondition(Long current, Long limit, String accountOrNickname);

    //修改用户邮箱和密码
    void setEmailAndPassword(String userId, String email, String password);

    //查询用户安全信息
    UserSecurity findAWEP(String userId);

    //查询出用户专栏、学习、说说、关注、粉丝数量
    Future<UserDetailVo> findCSDAFNumber(UserDetailVo userDetailVo, String userId);

    //查询用户基本信息
    void findDetailInformation(UserDetailVo userDetailVo, String userId);

    //查询用户发布文章数量、是否签到、vip等级
    Future<MyUserInfoVo> findUserRIV(String token);

    //用户签到
    void toSignIn(String userId);

    //查询用户vip、已经发布但没有违规的文章数量、评论数
    Future<HomePageVo> findVACNumber(String userId , String token);

    //用户账号密码登录
    String login(String loginAct, String loginPwd);

    //修改用户背景图像
    void setBgImg(String bgImg, String userId);
}
