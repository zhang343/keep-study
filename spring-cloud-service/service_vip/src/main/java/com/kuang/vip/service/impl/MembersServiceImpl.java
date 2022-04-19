package com.kuang.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.*;
import com.kuang.vip.client.UcenterClient;
import com.kuang.vip.entity.Members;
import com.kuang.vip.entity.Rights;
import com.kuang.vip.mapper.MembersMapper;
import com.kuang.vip.mapper.RightsMapper;
import com.kuang.vip.service.CacheService;
import com.kuang.vip.service.MembersService;
import com.kuang.vip.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class MembersServiceImpl extends ServiceImpl<MembersMapper, Members> implements MembersService {

    @Resource
    private RightsMapper rightsMapper;

    @Resource
    private CacheService cacheService;

    @Resource
    private MembersMapper membersMapper;

    @Resource
    private UcenterClient ucenterClient;

    //用户充值vip
    @Transactional
    @Override
    public void addMember(String vipId , String userId) {
        //检查该vip是否存在
        Rights rights = rightsMapper.selectById(vipId);
        if(rights == null || rights.getPrice() == 0){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        //检查该用户是否存在
        QueryWrapper<Members> membersWrapper = new QueryWrapper<>();
        membersWrapper.eq("user_id" , userId);
        Integer integer = baseMapper.selectCount(membersWrapper);
        if(integer != 0){
            throw new XiaoXiaException(ResultCode.ERROR , "你已经是vip的,不要充值");
        }

        //防止并发情况下多个线程抵达，利用分布式锁，让只有一个线程进来
        boolean b = RedisUtils.tryVipLock(userId, 60);
        if(b){
            //插入
            Members members = new Members();
            members.setUserId(userId);
            members.setRightsId(vipId);
            Date date = new Date(System.currentTimeMillis() + DateUtils.ONEDAY * rights.getTimeLength());
            members.setExpirationTime(date);
            int insert = membersMapper.insert(members);
            if(insert != 1){
                RedisUtils.unVipLock(userId);
                throw new XiaoXiaException(ResultCode.ERROR , "充值失败");
            }
            //扣除k币
            R reduce = ucenterClient.reduce(rights.getPrice() , userId);
            if(!reduce.getSuccess()){
                RedisUtils.unVipLock(userId);
                throw new XiaoXiaException(ResultCode.ERROR , "充值失败，请检查k币是否够");
            }
            //清除vip成员缓存
            RedisUtils.delKey(RedisUtils.ALLVIPMEMBERTREEMAP);
            //解锁
            RedisUtils.unVipLock(userId);
        }else {
            throw new XiaoXiaException(ResultCode.ERROR , "请不要频繁操作");
        }
    }


    //查询用户的viplogo
    @Override
    public Object findUserVipLevel(List<String> userIdList) {
        return VipUtils.getUserVipLevel(userIdList,
                cacheService.CacheAllMembersRedisTreeMap(),
                cacheService.CacheAllRightRedisTreeMap(),
                cacheService.CacheNotVipRight());
    }



}
