package com.kuang.springcloud.utils;

import com.kuang.springcloud.entity.MembersRedis;
import com.kuang.springcloud.entity.RightRedis;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class VipUtils {


    //获取非vip权益
    private static RightRedis getNotVipRight(){
        Object valueNotVipRight = RedisUtils.getValue(RedisUtils.NOTVIPRIGHT);
        if(valueNotVipRight == null){
            return null;
        }
        return (RightRedis) valueNotVipRight;
    }

    //获取所有权益
    private static TreeMap<String , RightRedis> getAllRightTreeMap(){
        Object valueRight = RedisUtils.getValue(RedisUtils.ALLRIGHTTREEMAP);
        if(valueRight == null){
            return null;
        }
        return (TreeMap<String, RightRedis>) valueRight;
    }


    //获取所有vip成员
    private static TreeMap<String , MembersRedis> getAllMembersTreeMap(){
        Object valueMember = RedisUtils.getValue(RedisUtils.ALLVIPMEMBERTREEMAP);
        if(valueMember == null){
            return null;
        }
        return (TreeMap<String, MembersRedis>) valueMember;
    }


    //查询出用户指定权益
    public static RightRedis getUserRightRedis(String userId) {
        TreeMap<String, MembersRedis> membersRedisTreeMap = getAllMembersTreeMap();
        TreeMap<String , RightRedis> rightRedisTreeMap = getAllRightTreeMap();
        RightRedis notVipRight = getNotVipRight();
        return getUserRightRedis(userId, membersRedisTreeMap, rightRedisTreeMap, notVipRight);
    }

    //查询出用户指定权益
    public static RightRedis getUserRightRedis(String userId ,
                                        TreeMap<String, MembersRedis> membersRedisTreeMap ,
                                        TreeMap<String , RightRedis> rightRedisTreeMap ,
                                        RightRedis notVipRightRedis) {
        //校验数据
        if(StringUtils.isEmpty(userId) || membersRedisTreeMap == null || rightRedisTreeMap == null || notVipRightRedis == null) {
            return null;
        }
        MembersRedis membersRedis = membersRedisTreeMap.get(userId);
        if(membersRedis == null){
            return notVipRightRedis;
        }

        String rightsId = membersRedis.getRightsId();
        return rightRedisTreeMap.get(rightsId);
    }


    //获取用户指定的vipLevel
    public static String getUserVipLevel(String userId){
        RightRedis userRightRedis = getUserRightRedis(userId);
        if(userRightRedis == null){
            return null;
        }
        return userRightRedis.getVipLevel();
    }


    //获取用户指定的vipLevel
    public static String getUserVipLevel(String userId ,
                                         TreeMap<String, MembersRedis> membersRedisTreeMap ,
                                         TreeMap<String , RightRedis> rightRedisTreeMap ,
                                         RightRedis notVipRightRedis){
        RightRedis userRightRedis = getUserRightRedis(userId , membersRedisTreeMap , rightRedisTreeMap , notVipRightRedis);
        if(userRightRedis == null){
            return null;
        }
        return userRightRedis.getVipLevel();
    }



    //获取用户指定的vipLevel
    public static Map<String , String> getUserVipLevel(List<String> userIdList){
        TreeMap<String, MembersRedis> membersRedisTreeMap = getAllMembersTreeMap();
        TreeMap<String , RightRedis> rightRedisTreeMap = getAllRightTreeMap();
        RightRedis notVipRight = getNotVipRight();
        return getUserVipLevel(userIdList, membersRedisTreeMap, rightRedisTreeMap, notVipRight);
    }


    //获取用户指定的vipLevel
    public static Map<String , String> getUserVipLevel(List<String> userIdList ,
                                                       TreeMap<String, MembersRedis> membersRedisTreeMap ,
                                                       TreeMap<String , RightRedis> rightRedisTreeMap ,
                                                       RightRedis notVipRight){
        if(userIdList == null || userIdList.size() == 0 || membersRedisTreeMap == null || rightRedisTreeMap == null || notVipRight == null){
            return null;
        }
        Map<String , String> map = new HashMap<>();
        for(String userId : userIdList){
            MembersRedis membersRedis = membersRedisTreeMap.get(userId);
            if(membersRedis == null){
                map.put(userId , notVipRight.getVipLevel());
                continue;
            }

            String rightsId = membersRedis.getRightsId();
            RightRedis rightRedis = rightRedisTreeMap.get(rightsId);
            map.put(userId , rightRedis.getVipLevel());
        }
        return map;
    }


    //此处要求对象存在两个方法setVipLevel和getUserId，两个参数类型一样
    public static void setVipLevel(Collection<?> objectList , Object o) {
        TreeMap<String, MembersRedis> membersRedisTreeMap = getAllMembersTreeMap();
        TreeMap<String , RightRedis> rightRedisTreeMap = getAllRightTreeMap();
        RightRedis notVipRight = getNotVipRight();
        if(membersRedisTreeMap == null || rightRedisTreeMap == null || notVipRight == null){
            return;
        }


        Method setVipLevel = null;
        Method getUserId = null;
        try {
            setVipLevel = o.getClass().getDeclaredMethod("setVipLevel", String.class);
            getUserId = o.getClass().getDeclaredMethod("getUserId");
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        }

        if(setVipLevel == null || getUserId == null){
            return;
        }





        for(Object object : objectList){
            String userId = null;
            try {
                userId = (String) getUserId.invoke(object);
            } catch(Exception e) {
                e.printStackTrace();
            }

            if(userId == null){
                continue;
            }

            MembersRedis membersRedis = membersRedisTreeMap.get(userId);
            if(membersRedis == null){
                try {
                    setVipLevel.invoke(object , notVipRight.getVipLevel());
                } catch(Exception e) {
                    e.printStackTrace();
                }
                continue;
            }

            String rightsId = membersRedis.getRightsId();
            RightRedis rightRedis = rightRedisTreeMap.get(rightsId);
            try {
                setVipLevel.invoke(object , rightRedis.getVipLevel());
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
}
