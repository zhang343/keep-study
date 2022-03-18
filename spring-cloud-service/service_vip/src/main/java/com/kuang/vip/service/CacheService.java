package com.kuang.vip.service;


import com.kuang.springcloud.entity.MembersRedis;
import com.kuang.springcloud.entity.RightRedis;

import java.util.List;
import java.util.TreeMap;

public interface CacheService {
    //缓存非vip权益数据
    RightRedis CacheNotVipRight();
    //缓存所有权益数据,以列表形式
    List<RightRedis> CacheAllRightRedisList();
    //缓存所有权益数据,以TreeMap形式,key为权益id
    TreeMap<String , RightRedis> CacheAllRightRedisTreeMap();
    //缓存vip成员，以TreeMap形式,key为userId
    TreeMap<String , MembersRedis> CacheAllMembersRedisTreeMap();
}
