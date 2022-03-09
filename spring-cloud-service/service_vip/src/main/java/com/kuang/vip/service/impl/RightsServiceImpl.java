package com.kuang.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.vip.entity.Members;
import com.kuang.vip.entity.Rights;
import com.kuang.vip.mapper.RightsMapper;
import com.kuang.vip.service.MembersService;
import com.kuang.vip.service.RightsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * @author Xiaozhang
 * @since 2022-02-07
 */
@Service
@Slf4j
public class RightsServiceImpl extends ServiceImpl<RightsMapper, Rights> implements RightsService {

    @Resource
    private MembersService membersService;

    //查询出vip权益,非普通会员权益
    @Override
    public List<Rights> findVipRight() {
        log.info("查询出vip权益,非普通会员权益,即价格不为0");
        Rights notVipRights = null;
        List<Rights> allRightList = findAllRight();
        for(Rights rights : allRightList){
            if(rights.getPrice() != 0){
                notVipRights = rights;
                break;
            }
        }
        if(notVipRights != null){
            allRightList.remove(notVipRights);
        }
        return allRightList;
    }

    //查询出非vip权益
    @Override
    public Rights findNotVipRight() {
        List<Rights> allRightList = findAllRight();
        for(Rights rights : allRightList){
            if(rights.getPrice() == 0){
                return rights;
            }
        }
        return null;
    }

    //查询出所有权益
    @Cacheable(value = "allRightList")
    @Override
    public List<Rights> findAllRight() {
        log.info("查询出vip表的所有权益");
        QueryWrapper<Rights> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "vip_level" , "price" , "course_discount" ,
                "column_number" , "sign_experience" , "money" , "article_number" , "time_length");
        return baseMapper.selectList(wrapper);
    }

    //设置所有权益,以权益vipId为key
    @Cacheable(value = "allRightTreeMap")
    @Override
    public TreeMap<String, Object> findAllRightTreeMap() {
        TreeMap<String , Object> treeMap = new TreeMap<>();
        List<Rights> rightsList = findAllRight();
        for(Rights rights : rightsList){
            treeMap.put(rights.getId() , rights);
        }
        return treeMap;
    }

    //查出用户对应课程打折数量
    @Override
    public double findUserCourseDiscount(String userId) {
        log.info("查出成员对应课程打折数量,用户id:" + userId);
        Rights rights = findRightByUserId(userId);
        if(rights == null){
            return 1.0;
        }
        return rights.getCourseDiscount();
    }

    //查询出指定用户权益
    @Override
    public Rights findRightByUserId(String userId) {
        log.info("查询指定用户权益,用户id:" + userId);
        TreeMap<String , Object> allVipMember = membersService.findAllVipMemberTreeMap();
        Object o = allVipMember.get(userId);
        if(o == null){
            return findNotVipRight();
        }
        Members members = (Members) o;
        TreeMap<String, Object> allRight = findAllRightTreeMap();
        Object o1 = allRight.get(members.getRightsId());
        if(o1 == null){
            return null;
        }
        return (Rights) o1;
    }


}
