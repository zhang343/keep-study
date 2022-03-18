package com.kuang.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
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

/**
 * @author Xiaozhang
 * @since 2022-02-07
 */
@Service
@Slf4j
public class MembersServiceImpl extends ServiceImpl<MembersMapper, Members> implements MembersService {

    @Resource
    private UcenterClient ucenterClient;

    @Resource
    private RightsMapper rightsMapper;

    @Resource
    private CacheService cacheService;

    //用户充值vip
    @Transactional
    @Override
    public void addMember(String vipId , String userId) {
        log.info("开始充值vip,vip的id为:" + vipId);
        //检查该vip是否存在
        QueryWrapper<Rights> wrapper = new QueryWrapper<>();
        wrapper.eq("id" , vipId);
        Rights rights = rightsMapper.selectOne(wrapper);
        if(rights == null || rights.getPrice() == 0){
            log.warn("有人非法充值不存在的vip或者该vip是非vip,vip的id:" + vipId);
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        //检查该用户是否存在
        QueryWrapper<Members> membersWrapper = new QueryWrapper<>();
        membersWrapper.eq("user_id" , userId);
        Integer integer = baseMapper.selectCount(membersWrapper);
        if(integer != 0){
            log.warn("用户已经vip,还要充值,非法,用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "你已经是vip的,不要充值");
        }
        Members members = new Members();
        members.setUserId(userId);
        members.setRightsId(vipId);
        //设置vip的到期时间
        Date expirationTime = new Date(System.currentTimeMillis() + DateUtils.ONEDAY * rights.getTimeLength());
        members.setExpirationTime(expirationTime);
        int insert = baseMapper.insert(members);
        if(insert != 1){
            log.error("数据插入vip_members库失败,请检查数据库");
            throw new XiaoXiaException(ResultCode.ERROR , "充值vip失败");
        }
        int price = rights.getPrice();
        //远程调用减去用户k币
        R ucenterR = ucenterClient.reduce(price);
        if(!ucenterR.getSuccess()){
            throw new XiaoXiaException(ResultCode.ERROR , "充值vip失败");
        }
        //清空redis缓存
        RedisUtils.delKey(RedisUtils.ALLVIPMEMBERTREEMAP);
        cacheService.CacheAllMembersRedisTreeMap();
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
