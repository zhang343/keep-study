package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.*;
import com.kuang.ucenter.client.BbsClient;
import com.kuang.ucenter.client.VipClient;
import com.kuang.ucenter.entity.UserInfo;
import com.kuang.ucenter.entity.vo.MyUserInfoVo;
import com.kuang.ucenter.mapper.UserInfoMapper;
import com.kuang.ucenter.service.UserAttentionService;
import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private VipClient vipClient;

    @Resource
    private BbsClient bbsClient;

    @Resource
    private UserAttentionService attentionService;

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

        Future<MyUserInfoVo> userbbsArticleNumberAndIsSign = findUserbbsArticleNumberAndIsSign(userId);

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
            myUserInfoVo.setIsSignIn(myUserInfoVo1.getIsSignIn());
            myUserInfoVo.setBbsArticleNumber(myUserInfoVo1.getBbsArticleNumber());
        }catch(Exception e){
            log.warn("异步任务执行失败");
        }

        return myUserInfoVo;
    }

    //设置用户江湖文章数量和是否签到
    @Async
    public Future<MyUserInfoVo> findUserbbsArticleNumberAndIsSign(String userId){
        MyUserInfoVo myUserInfoVo = new MyUserInfoVo();
        R userIsSign = vipClient.findUserIsSign(userId);
        boolean isSignIn = false;
        if(userIsSign.getSuccess()){
            isSignIn = (boolean) userIsSign.getData().get("isSign");
        }
        myUserInfoVo.setIsSignIn(isSignIn);
        R userbbsArticleNumber = bbsClient.findUserbbsArticleNumber(userId);
        Integer bbsArticleNumber = 0;
        if(userbbsArticleNumber.getSuccess()){
            bbsArticleNumber = (Integer) userbbsArticleNumber.getData().get("articleNumber");
        }
        myUserInfoVo.setBbsArticleNumber(bbsArticleNumber);
        return new AsyncResult<>(myUserInfoVo);

    }


}
