package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.MD5Util;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.RedisUtils;
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




}
