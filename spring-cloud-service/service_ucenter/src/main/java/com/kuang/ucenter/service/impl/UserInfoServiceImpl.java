package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.*;
import com.kuang.ucenter.client.BbsClient;
import com.kuang.ucenter.client.CourseClient;
import com.kuang.ucenter.client.VipClient;
import com.kuang.ucenter.entity.UserInfo;
import com.kuang.ucenter.entity.vo.*;
import com.kuang.ucenter.mapper.UserInfoMapper;
import com.kuang.ucenter.service.UserAttentionService;
import com.kuang.ucenter.service.UserColumnService;
import com.kuang.ucenter.service.UserInfoService;
import com.kuang.ucenter.service.UserTalkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private BbsClient bbsClient;

    @Resource
    private VipClient vipClient;

    @Resource
    private CourseClient courseClient;

    @Resource
    private UserAttentionService attentionService;

    @Resource
    private UserColumnService columnService;

    @Resource
    private UserTalkService talkService;

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
    @Override
    public int findUserNumber() {
        Object value = RedisUtils.getValue(RedisUtils.AllUSERNUMBER);
        if(value != null){
            return (Integer) value;
        }
        Integer integer = baseMapper.selectCount(null);
        RedisUtils.setValueTimeout(RedisUtils.AllUSERNUMBER , integer , 120);
        return integer;
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

    //用户登录之后查询小方框内容
    @Override
    public MyUserInfoVo findUserSmallBoxContent(String userId) {

        Future<MyUserInfoVo> userbbsArticleNumberAndIsSign = findUserbbsArticleNumber(userId);

        MyUserInfoVo myUserInfoVo = new MyUserInfoVo();
        UserInfo userInfo = baseMapper.selectById(userId);
        BeanUtils.copyProperties(userInfo , myUserInfoVo);
        String userVipLevel = VipUtils.getUserVipLevel(userId);
        Integer attentionNumber = attentionService.findUserFollowNumber(userId);
        Integer fansNumber = attentionService.findUserFansNumber(userId);
        myUserInfoVo.setVipLevel(userVipLevel);
        myUserInfoVo.setAttentionNumber(attentionNumber);
        myUserInfoVo.setFansNumber(fansNumber);


        try {
            MyUserInfoVo myUserInfoVo1 = userbbsArticleNumberAndIsSign.get(200, TimeUnit.MILLISECONDS);
            myUserInfoVo.setBbsArticleNumber(myUserInfoVo1.getBbsArticleNumber());
        }catch(Exception e){
            log.warn("异步任务执行失败");
        }

        return myUserInfoVo;
    }

    //查询用户主页的内容,这里查自己
    @Override
    public UserDetailVo findUserHomePage(String userId) {
        //查询用户关注、粉丝、专栏、学习、说说、vip
        Future<UserDetailVo> afcsd = findAFCSDV(userId);

        //查询基本信息
        UserInfo userInfo = baseMapper.selectById(userId);
        UserDetailVo userDetailVo = new UserDetailVo();
        BeanUtils.copyProperties(userInfo , userDetailVo);

        //查询用户所有江湖文章
        R userAllArticleNumber = bbsClient.findUserAllArticleNumber(userId);
        Integer allArticleNumber = 0;
        if(userAllArticleNumber.getSuccess()){
            allArticleNumber = (Integer) userAllArticleNumber.getData().get("userAllArticleNumber");
        }
        userDetailVo.setAllArticleNumber(allArticleNumber);

        //查询用户在江湖可以被别人看见的文章数量
        R userbbsArticleNumber = bbsClient.findUserbbsArticleNumber(userId);
        Integer bbsArticleNumber = 0;
        if(userbbsArticleNumber.getSuccess()){
            bbsArticleNumber = (Integer) userbbsArticleNumber.getData().get("articleNumber");
        }
        userDetailVo.setBbsArticleNumber(bbsArticleNumber);


        Integer commentNumber = 0;
        R userCommentNumber = bbsClient.findUserCommentNumber(userId);
        if(userCommentNumber.getSuccess()){
            commentNumber = (Integer) userCommentNumber.getData().get("commentNumber");
        }
        userDetailVo.setCommentNumber(commentNumber);


        try {
            UserDetailVo userDetailVo1 = afcsd.get();
            userDetailVo.setFansNumber(userDetailVo1.getFansNumber());
            userDetailVo.setAttentionNumber(userDetailVo1.getAttentionNumber());
            userDetailVo.setStudyNumber(userDetailVo1.getStudyNumber());
            userDetailVo.setColumnNumber(userDetailVo1.getColumnNumber());
            userDetailVo.setDynamicNumber(userDetailVo1.getDynamicNumber());
            userDetailVo.setVipLevel(userDetailVo1.getVipLevel());
        }catch(Exception e){
            log.warn("查询用户关注、粉丝、专栏、学习、说说数量失败");
        }

        return userDetailVo;
    }


    //查询用户关注、粉丝、专栏、学习、说说数量、vip
    @Async
    public Future<UserDetailVo> findAFCSDV(String userId){
        UserDetailVo userDetailVo = new UserDetailVo();
        Integer userFollowNumber = attentionService.findUserFollowNumber(userId);
        Integer userFansNumber = attentionService.findUserFansNumber(userId);
        Integer columnNumber = columnService.findUserColumnNumber(userId);
        R userStudyNumber = courseClient.findUserStudyNumber(userId);
        Integer studyNumber = 0;
        if(userStudyNumber.getSuccess()){
            studyNumber = (Integer) userStudyNumber.getData().get("studyNumber");
        }
        Integer dynamicNumber = talkService.findUserTalkNumber(userId);
        //查询vip
        String userVipLevel = VipUtils.getUserVipLevel(userId);
        if(userVipLevel == null){
            R rightRedisByUserId = vipClient.findRightRedisByUserId(userId);
            if(rightRedisByUserId.getSuccess()){
                RightRedis rightRedis = (RightRedis) rightRedisByUserId.getData().get("rightRedis");
                userVipLevel = rightRedis.getVipLevel();
            }
        }
        userDetailVo.setFansNumber(userFansNumber);
        userDetailVo.setAttentionNumber(userFollowNumber);
        userDetailVo.setColumnNumber(columnNumber);
        userDetailVo.setStudyNumber(studyNumber);
        userDetailVo.setDynamicNumber(dynamicNumber);
        userDetailVo.setVipLevel(userVipLevel);
        return new AsyncResult<>(userDetailVo);
    }

    //用户签到
    @Transactional
    @Override
    public int userSignIn(String userId) {
        RightRedis userRightRedis = VipUtils.getUserRightRedis(userId);
        if(userRightRedis == null){
            throw new XiaoXiaException(ResultCode.ERROR , "签到失败");
        }
        Integer signExperience = userRightRedis.getSignExperience();
        UserInfo userInfo = baseMapper.selectById(userId);
        if(userInfo.getIsSign()){
            throw new XiaoXiaException(ResultCode.ERROR , "你已经签到了");
        }
        UserInfo userInfoUpdate = new UserInfo();
        userInfoUpdate.setId(userId);
        userInfoUpdate.setExperience(userInfo.getExperience() + signExperience);
        userInfoUpdate.setIsSign(true);
        userInfoUpdate.setVersion(userInfo.getVersion());
        int i = baseMapper.updateById(userInfoUpdate);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "签到失败");
        }
        return signExperience;
    }

    //更新用户每日签到
    @Override
    public void updateUserIsSign() {
        baseMapper.updateUserIsSign();
    }

    //修改用户背景图像
    @Override
    public void setUserBgimg(String userId, String url) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setBgImg(url);
        int i = baseMapper.updateById(userInfo);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改背景图像失败");
        }
    }

    //查询出用户资料
    @Override
    public UserDateVo findUserData(String userId) {
        UserInfo userInfo = baseMapper.selectById(userId);
        UserDateVo userDateVo = new UserDateVo();
        BeanUtils.copyProperties(userInfo , userDateVo);
        return userDateVo;
    }

    //修改用户资料
    @Override
    public void setUserData(String userId, UserSetDataVo userSetDataVo) {
        if(StringUtils.isEmpty(userSetDataVo.getAddress()) &&
                StringUtils.isEmpty(userSetDataVo.getSex()) &&
                StringUtils.isEmpty(userSetDataVo.getAvatar()) &&
                StringUtils.isEmpty(userSetDataVo.getNickname()) &&
                StringUtils.isEmpty(userSetDataVo.getSign())){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要传入空值");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        BeanUtils.copyProperties(userSetDataVo , userInfo);
        int i = baseMapper.updateById(userInfo);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改失败");
        }
    }

    //查询用户安全信息
    @Override
    public UserSecurity findUserSecurityData(String userId) {
        UserInfo userInfo = baseMapper.selectById(userId);
        UserSecurity userSecurity = new UserSecurity();
        BeanUtils.copyProperties(userInfo , userSecurity);
        return userSecurity;
    }

    //修改用户安全信息
    @Override
    public void setUserSecurityData(String userId, String email, String password) {
        if(StringUtils.isEmpty(email) && StringUtils.isEmpty(password)){
            throw new XiaoXiaException(ResultCode.ERROR , "不可以为空");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setEmail(email);
        userInfo.setPassword(MD5Util.getMD5(password));
        int i = baseMapper.updateById(userInfo);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改失败");
        }
    }

    //根据条件查找用户数量
    @Override
    public Integer findUserByAccountOrNicknameNumber(String accountOrNickname) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.like("account" , accountOrNickname).or().like("nickname" , accountOrNickname);
        return baseMapper.selectCount(wrapper);
    }

    //根据条件查找用户
    @Override
    public List<UserSearchVo> findUserByAccountOrNickname(String accountOrNickname , Long current , Long limit) {
        current = (current - 1) * limit;
        return baseMapper.findUserByAccountOrNickname(accountOrNickname , current , limit);
    }

    //设置用户江湖文章数量
    @Async
    public Future<MyUserInfoVo> findUserbbsArticleNumber(String userId){
        MyUserInfoVo myUserInfoVo = new MyUserInfoVo();
        R userbbsArticleNumber = bbsClient.findUserbbsArticleNumber(userId);
        Integer bbsArticleNumber = 0;
        if(userbbsArticleNumber.getSuccess()){
            bbsArticleNumber = (Integer) userbbsArticleNumber.getData().get("articleNumber");
        }
        myUserInfoVo.setBbsArticleNumber(bbsArticleNumber);
        return new AsyncResult<>(myUserInfoVo);

    }


}
