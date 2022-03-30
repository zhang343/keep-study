package com.kuang.bbs.utils;

import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.utils.RedisUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ColumnUtils {
    //所有人
    public static final long EVERYONE = 0;
    //自己
    public static final long OWN = 1;
    //会员
    public static final long VIPMEMBER = 2;
    //年会员
    public static final long YEARVIPMEMBER = 3;
    //终身会员
    public static final long LOFELONGVIPMEMBER = 4;

    public static final List<Long> vsibility = new ArrayList<>(Arrays.asList(EVERYONE , OWN , VIPMEMBER , YEARVIPMEMBER , LOFELONGVIPMEMBER));

    //对权益进行相应的排序
    private static List<RightRedis> getAllSortRight(){
        Object value = RedisUtils.getValue(RedisUtils.ALLRIGHTLIST);
        if(value != null){
            List<RightRedis> rightRedisList = (List<RightRedis>) value;
            Collections.sort(rightRedisList);
            return rightRedisList;
        }
        return null;
    }

    //取出会员权益
    private static List<String> getAllVipRight(){
        Object value = RedisUtils.getValue(RedisUtils.ALLRIGHTLIST);
        if(value != null){
            List<RightRedis> rightRedisList = (List<RightRedis>) value;
            List<String> idList = new ArrayList<>();
            for(RightRedis rightRedis : rightRedisList){
                if(rightRedis.getPrice() != 0){
                    idList.add(rightRedis.getId());
                }
            }
            return idList;
        }
        return null;
    }

    //取出年会员权益
    private static List<String> getYearVipRight(){
        List<RightRedis> allSortRight = getAllSortRight();
        if(allSortRight != null){
            List<String> idList = new ArrayList<>();
            idList.add(allSortRight.get(1).getId());
            return idList;
        }
        return null;
    }

    //取出终身会员权益
    private static List<String> getLifeLongVipRight(){
        List<RightRedis> allSortRight = getAllSortRight();
        if(allSortRight != null){
            List<String> idList = new ArrayList<>();
            idList.add(allSortRight.get(0).getId());
            return idList;
        }
        return null;
    }

    //校验会员权益
    public static boolean checkAllVipRight(String id){
        List<String> allVipRight = getAllVipRight();
        if(allVipRight == null){
            return false;
        }
        return allVipRight.contains(id);
    }

    //校验年会员权益
    public static boolean checkYearVipRight(String id){
        List<String> yearVipRight = getYearVipRight();
        if(yearVipRight == null){
            return false;
        }
        return yearVipRight.contains(id);
    }

    //校验终身会员权益
    public static boolean checkLifeLongVipRight(String id){
        List<String> lifeLongVipRight = getLifeLongVipRight();
        if(lifeLongVipRight == null){
            return false;
        }
        return lifeLongVipRight.contains(id);
    }
}
