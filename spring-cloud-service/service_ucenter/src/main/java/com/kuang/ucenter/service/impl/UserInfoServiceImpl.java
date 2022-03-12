package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.MD5Util;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.client.BbsClient;
import com.kuang.ucenter.client.VipClient;
import com.kuang.ucenter.entity.UserInfo;
import com.kuang.ucenter.entity.vo.*;
import com.kuang.ucenter.mapper.UserInfoMapper;
import com.kuang.ucenter.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    @Resource
    private UserColumnService userColumnService;

    @Resource
    private UserStudyService userStudyService;

    @Resource
    private UserTalkService userTalkService;

    @Resource
    private UserAttentionService userAttentionService;

    @Resource
    private BbsClient bbsClient;

    @Resource
    private VipClient vipClient;


    //根据微信id查询用户
    @Override
    public UserInfo getOpenIdMember(String openid) {
        log.info("根据openid查询用户：" + openid);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("openid" , openid);
        return baseMapper.selectOne(wrapper);
    }

    //创建一个用户
    @Override
    public void insertMember(UserInfo member) {
        log.info("创建一个用户，该用户openid：" + member.getOpenid());
        int insert = baseMapper.insert(member);
        if(insert != 1){
            log.info("创建一个用户失败，该用户openid：" + member.getOpenid());
            throw new XiaoXiaException(ResultCode.ERROR , "创建用户失败");
        }
    }

    //查询系统用户数量
    @Cacheable(value = "userNumber")
    @Override
    public int findUserNumber() {
        log.info("开始查询系统用户数量");
        return baseMapper.selectCount(null);
    }

    //查询用户头像和昵称
    @Override
    public UserInfo findAvatarAndNicknameByUserId(String userId) {
        log.info("查询用户头像和昵称,用户id：" + userId);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.select("avatar" , "nickname");
        wrapper.eq("id" , userId);
        return baseMapper.selectOne(wrapper);
    }

    //给用户增加k币
    @Override
    public void addKCoin(Integer kCoinNumber, String userId) {
        log.info("给用户:" + userId + "增加" + kCoinNumber + "k币");
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "money" , "version");
        wrapper.eq("id" , userId);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        int result = userInfo.getMoney() + kCoinNumber;
        if(result < 0){
            log.warn("用户k币数量:" + userInfo.getMoney() + ",减少k币数量:" + kCoinNumber + ",用户k币数量不足");
            throw new XiaoXiaException(ResultCode.ERROR , "k币数量不足");
        }
        userInfo.setMoney(result);
        int num = baseMapper.updateById(userInfo);
        if(num != 1){
            log.error("更新用户k币出错,数据库可能出现错误");
            throw new XiaoXiaException(ResultCode.ERROR , "增加用户k币失败");
        }
    }

    //查找用户数量，通过条件
    @Override
    public Long findUserNumberByCondition(String accountOrNickname) {
        log.info("开始查询用户数量,用户账号或者昵称为:" + accountOrNickname);
        return baseMapper.findUserNumberByCondition(accountOrNickname);
    }

    //查找用户，通过条件
    @Override
    public List<UserSearchVo> findUserByCondition(Long current, Long limit, String accountOrNickname) {
        log.info("开始查询用户,用户账号或者昵称为:" + accountOrNickname);
        current = (current - 1) * limit;
        return baseMapper.findUserByCondition(current , limit , accountOrNickname);
    }

    //修改用户邮箱和密码
    @Override
    public void setEmailAndPassword(String userId, String email, String password) {
        log.info("设置用户邮箱和密码,用户id:" + userId + ",邮箱:" + email + "密码:" + password);
        if(StringUtils.isEmpty(email)){
            email = null;
        }
        if(StringUtils.isEmpty(password)){
            password = null;
        }else {
            password = MD5Util.getMD5(password);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setEmail(email);
        userInfo.setPassword(password);
        int i = baseMapper.updateById(userInfo);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改失败");
        }
    }

    //查询用户安全信息
    @Override
    public UserSecurity findAWEP(String userId) {
        log.info("查询用户安全信息,用户id:" + userId);
        UserSecurity userSecurity = baseMapper.findAWEP(userId);
        if(userSecurity == null){
            throw new XiaoXiaException(ResultCode.ERROR , "没有查找到用户安全信息");
        }
        return userSecurity;
    }

    //查询出用户专栏、学习、说说、关注、粉丝数量
    @Async
    @Override
    public Future<UserDetailVo> findCSDAFNumber(UserDetailVo userDetailVo, String userId) {
        log.info("查询出用户专栏、学习、说说、关注、粉丝数量");
        Integer columnNumber = userColumnService.findColumnNumberByUserId(userId);
        Integer studyNumber = userStudyService.findStudyNumberByUserId(userId);
        Integer dynamicNumber = userTalkService.findDynamicNumberByUserId(userId);
        Integer fansNumber = userAttentionService.findUserFansNumber(userId);
        Integer attentionNumber = userAttentionService.findUserAttentionNumber(userId);
        userDetailVo.setColumnNumber(columnNumber);
        userDetailVo.setStudyNumber(studyNumber);
        userDetailVo.setDynamicNumber(dynamicNumber);
        userDetailVo.setFansNumber(fansNumber);
        userDetailVo.setAttentionNumber(attentionNumber);
        return new AsyncResult<>(userDetailVo);
    }

    //查询用户基本信息
    @Override
    public void findDetailInformation(UserDetailVo userDetailVo, String userId) {
        log.info("查询用户基本信息");
        UserDetailVo userDetailVo1 = baseMapper.findDetailInformation(userId);
        if(userDetailVo1 == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        BeanUtils.copyProperties(userDetailVo1 , userDetailVo ,
                "attentionNumber" , "fansNumber" , "articleNumber" ,
                "columnNumber" , "studyNumber" , "dynamicNumber");
    }

    //查询用户发布文章数量、是否签到、vip等级
    @Async
    @Override
    public Future<MyUserInfoVo> findUserRIV(String token) {
        log.info("查询用户发布文章数量、是否签到、vip等级");
        R releaseArticleNumberR = bbsClient.findUserReleaseArticleNumber(token);
        R memberRightVipLevelAndIsSign = vipClient.findMemberRightVipLevelAndIsSign(token);
        Integer releaseArticleNumber = 0;
        if(releaseArticleNumberR.getSuccess()){
            releaseArticleNumber = (Integer) releaseArticleNumberR.getData().get("releaseArticleNumber");
        }

        String vipLevel = null;
        Boolean isSignIn = false;
        if(memberRightVipLevelAndIsSign.getSuccess()){
            vipLevel = (String) memberRightVipLevelAndIsSign.getData().get("vipLevel");
            isSignIn = (Boolean) memberRightVipLevelAndIsSign.getData().get("isSign");
        }
        MyUserInfoVo myUserInfoVo = new MyUserInfoVo();
        myUserInfoVo.setReleaseArticleNumber(releaseArticleNumber);
        myUserInfoVo.setVipLevel(vipLevel);
        myUserInfoVo.setIsSignIn(isSignIn);
        return new AsyncResult<>(myUserInfoVo);
    }

    //用户签到
    @Transactional
    @Override
    public void toSignIn(String userId) {
        log.info("用户签到,用户id:" + userId);
        R signExperienceR = vipClient.findSignExperience();
        if(!signExperienceR.getSuccess()){
            throw new XiaoXiaException(ResultCode.ERROR , "签到失败");
        }
        Integer signExperience = (Integer) signExperienceR.getData().get("signExperience");
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "experience" , "version");
        wrapper.eq("id" , userId);
        wrapper.eq("is_disabled" , 0);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        if(userInfo == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        userInfo.setExperience(userInfo.getExperience() + signExperience);
        int i = baseMapper.updateById(userInfo);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "签到失败");
        }
        R r = vipClient.toSign();
        if(!r.getSuccess()){
            throw new XiaoXiaException(ResultCode.ERROR , "签到失败");
        }
    }

    //查询用户vip、已经发布但没有违规的文章数量、评论数
    @Async
    @Override
    public Future<HomePageVo> findVACNumber(String userId , String token) {
        log.info("查询用户vip、已经发布但没有违规的文章数量、评论数");
        R memberRightVipLevel = vipClient.findMemberRightVipLevel(userId);
        if(!memberRightVipLevel.getSuccess()){
            throw new XiaoXiaException(ResultCode.ERROR , "查询失败");
        }
        R uranAndCN = bbsClient.findURANAndCN(token);

        Integer releaseArticleNumber = 0;
        Integer commentNumber = 0;
        if(uranAndCN.getSuccess()){
            releaseArticleNumber = (Integer) uranAndCN.getData().get("releaseArticleNumber");
            commentNumber = (Integer) uranAndCN.getData().get("commentNumber");
        }
        String vipLevel = (String) memberRightVipLevel.getData().get("vipLevel");
        HomePageVo homePageVo = new HomePageVo();
        homePageVo.setVipLevel(vipLevel);
        homePageVo.setArticleRealeaseNumber(releaseArticleNumber);
        homePageVo.setCommentNumber(commentNumber);
        return new AsyncResult<>(homePageVo);
    }

    //用户账号密码登录
    @Override
    public String login(String loginAct, String loginPwd) {
        log.info("用户登录,账号:" + loginAct + ",密码:" + loginPwd);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("account" , loginAct);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        if(userInfo == null){
            throw new XiaoXiaException(ResultCode.ACCOUNTERROR , "账号错误");
        }
        loginPwd = MD5Util.getMD5(loginPwd);
        String password = userInfo.getPassword();
        if(!loginPwd.equals(password)){
            throw new XiaoXiaException(ResultCode.PASSWORDERROR , "密码错误");
        }
        return userInfo.getId();
    }

    //修改用户背景图像
    @Override
    public void setBgImg(String bgImg, String userId) {
        log.info("修改用户背景图像,用户id:" + userId + ",背景图片地址:" + bgImg);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setBgImg(bgImg);
        int i = baseMapper.updateById(userInfo);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改用户背景失败");
        }
    }

    //用户设置修改资料里面的查询
    @Override
    public UserSetDataVo setdataquery(String userId) {
        log.info("用户设置修改资料里面的查询,用户id:" + userId);
        UserInfo userInfo = baseMapper.selectById(userId);
        if(userInfo == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        UserSetDataVo userSetDataVo = new UserSetDataVo();
        BeanUtils.copyProperties(userInfo , userSetDataVo);
        return userSetDataVo;
    }

    //设置用户头像
    @Override
    public void setUserHeadPortrait(String url, String userId) {
        log.info("用户设置头像,用户id:" + userId);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setAvatar(url);
        int i = baseMapper.updateById(userInfo);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改头像失败");
        }
    }

    //用户设置修改资料里面的修改
    @Override
    public void setdataupdate(String nickname, Boolean sex, String address, String sign , String userId) {
        log.info("用户设置修改资料里面的修改,用户id:" + userId);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        if(!StringUtils.isEmpty(nickname)){
            userInfo.setNickname(nickname);
        }
        if(!StringUtils.isEmpty(address)){
            userInfo.setAddress(address);
        }
        if(!StringUtils.isEmpty(sign)){
            userInfo.setSign(sign);
        }
        userInfo.setSex(sex);
        int i = baseMapper.updateById(userInfo);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改失败");
        }
    }
}
