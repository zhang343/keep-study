package com.kuang.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import com.kuang.vip.client.UcenterClient;
import com.kuang.vip.entity.UserTodayRight;
import com.kuang.vip.mapper.UserTodayRightMapper;
import com.kuang.vip.service.CacheService;
import com.kuang.vip.service.UserTodayRightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Xiaozhang
 * @since 2022-02-27
 */
@Service
@Slf4j
public class UserTodayRightServiceImpl extends ServiceImpl<UserTodayRightMapper, UserTodayRight> implements UserTodayRightService {

    @Resource
    private CacheService cacheService;

    @Resource
    private UcenterClient ucenterClient;

    //用户每日文章权益
    @Transactional
    @Override
    public void addArticle(String userId) {
        log.info("用户发布文章,每日文章权益减1,用户id:" + userId);
        RightRedis userRightRedis = VipUtils.getUserRightRedis(userId,
                cacheService.CacheAllMembersRedisTreeMap(),
                cacheService.CacheAllRightRedisTreeMap(),
                cacheService.CacheNotVipRight());

        QueryWrapper<UserTodayRight> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "money" , "article_number" , "version");
        wrapper.eq("user_id" , userId);
        UserTodayRight userTodayRight = baseMapper.selectOne(wrapper);
        if(userTodayRight == null || userRightRedis == null){
            log.warn("有人试图非法操作,减去用户每日文章权益,用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }

        //看用户发文上限是否到了
        if(userTodayRight.getArticleNumber() + 1 > userRightRedis.getArticleNumber()){
            log.warn("用户每日权益已经用完了,不能发文章了,用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }

        userTodayRight.setArticleNumber(userTodayRight.getArticleNumber() + 1);
        //看用户k币上限是否到了
        if(userTodayRight.getMoney() + 10 > userRightRedis.getMoney()){
            userTodayRight.setMoney(null);
            int i = baseMapper.updateById(userTodayRight);
            if(i != 1){
                log.warn("更新每日用户权益失败,用户id:" + userId);
                throw new XiaoXiaException(ResultCode.ERROR , "更新每日用户权益失败");
            }
            return;
        }

        //用户k币上限未到
        userTodayRight.setMoney(userTodayRight.getMoney() + 10);
        int i = baseMapper.updateById(userTodayRight);
        if(i != 1){
            log.warn("更新每日用户权益失败,用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "更新每日用户权益失败");
        }

        //远程调用，给用户增加k币
        R ucenterR = ucenterClient.add(10);
        if(!ucenterR.getSuccess()){
            log.error("远程调用service-ucenter下面的接口/KCoin/add失败，未增加用户k币");
            throw new XiaoXiaException(ResultCode.ERROR , "增加用户k币失败");
        }

    }

    //更新用户每日权益
    @Override
    public boolean updateMemberTodayRight() {
        log.info("更新用户每日权益");
        int integer = baseMapper.selectCount(null);
        int num = baseMapper.updateMemberTodayRight();
        return integer == num;
    }

    //查看用户是否签到
    @Override
    public Boolean findIsSign(String userId) {
        log.info("查看用户是否签到");
        QueryWrapper<UserTodayRight> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        UserTodayRight userTodayRight = baseMapper.selectOne(wrapper);
        if(userTodayRight == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        return userTodayRight.getIsSign();
    }

    //用户签到
    @Override
    public void toSign(String userId) {
        log.info("用户签到接口权益用户id:" + userId);
        QueryWrapper<UserTodayRight> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "is_sign");
        wrapper.eq("user_id" , userId);
        UserTodayRight userTodayRight = baseMapper.selectOne(wrapper);
        if(userTodayRight == null || userTodayRight.getIsSign()){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        userTodayRight.setIsSign(true);
        int i = baseMapper.updateById(userTodayRight);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "签到失败");
        }
    }

}
