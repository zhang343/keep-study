package com.kuang.vip.service.impl;

import com.kuang.springcloud.entity.MembersRedis;
import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.vip.mapper.MembersMapper;
import com.kuang.vip.mapper.RightsMapper;
import com.kuang.vip.service.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.TreeMap;


@Service
public class CacheServiceImpl implements CacheService {

    @Resource
    private RightsMapper rightsMapper;

    @Resource
    private MembersMapper membersMapper;

    //缓存非vip权益数据
    @Override
    public RightRedis CacheNotVipRight() {
        Object value = RedisUtils.getValue(RedisUtils.NOTVIPRIGHT);
        if(value != null){
            return (RightRedis) value;
        }
        RightRedis rightRedis = rightsMapper.findNotVipRight();
        RedisUtils.setValueTimeout(RedisUtils.NOTVIPRIGHT , rightRedis , 120);
        return rightRedis;
    }

    //缓存所有权益数据,以列表形式
    @Override
    public List<RightRedis> CacheAllRightRedisList() {
        Object value = RedisUtils.getValue(RedisUtils.ALLRIGHTLIST);
        if(value != null){
            return (List<RightRedis>) value;
        }
        List<RightRedis> rightRedisList = rightsMapper.findAllRight();
        RedisUtils.setValueTimeout(RedisUtils.ALLRIGHTLIST , rightRedisList , 120);
        return rightRedisList;
    }

    //缓存所有权益数据,以TreeMap形式,key为权益id
    @Override
    public TreeMap<String, RightRedis> CacheAllRightRedisTreeMap() {
        Object value = RedisUtils.getValue(RedisUtils.ALLRIGHTTREEMAP);
        if(value != null){
            return (TreeMap<String, RightRedis>) value;
        }
        List<RightRedis> rightRedisList = rightsMapper.findAllRight();
        TreeMap<String , RightRedis> rightRedisTreeMap = new TreeMap<>();
        for(RightRedis rightRedis : rightRedisList){
            String id = rightRedis.getId();
            rightRedisTreeMap.put(id , rightRedis);
        }
        RedisUtils.setValueTimeout(RedisUtils.ALLRIGHTTREEMAP , rightRedisTreeMap , 120);
        return rightRedisTreeMap;
    }


    //缓存vip成员，以TreeMap形式,key为userId
    @Override
    public TreeMap<String, MembersRedis> CacheAllMembersRedisTreeMap() {
        Object value = RedisUtils.getValue(RedisUtils.ALLVIPMEMBERTREEMAP);
        if(value != null){
            return (TreeMap<String, MembersRedis>) value;
        }
        List<MembersRedis> membersRedisList = membersMapper.findAllMembers();
        TreeMap<String, MembersRedis> membersRedisTreeMap = new TreeMap<>();
        for(MembersRedis membersRedis : membersRedisList){
            String userId = membersRedis.getUserId();
            membersRedisTreeMap.put(userId , membersRedis);
        }
        RedisUtils.setValueTimeout(RedisUtils.ALLVIPMEMBERTREEMAP , membersRedisTreeMap , 120);
        return membersRedisTreeMap;
    }
}
