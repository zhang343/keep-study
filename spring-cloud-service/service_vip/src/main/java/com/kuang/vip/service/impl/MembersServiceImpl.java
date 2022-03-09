package com.kuang.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vip.client.UcenterClient;
import com.kuang.vip.entity.Members;
import com.kuang.vip.entity.Rights;
import com.kuang.vip.mapper.MembersMapper;
import com.kuang.vip.mapper.RightsMapper;
import com.kuang.vip.service.MembersService;
import com.kuang.vip.service.RightsService;
import com.kuang.vip.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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
    private RightsService rightsService;

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
        members.setExpirationTime(new Date(System.currentTimeMillis() + DateUtils.ONEDAY * rights.getTimeLength()));
        int insert = baseMapper.insert(members);
        if(insert != 1){
            log.error("数据插入vip_members库失败,请检查数据库");
            throw new XiaoXiaException(ResultCode.ERROR , "充值vip失败");
        }
        Integer price = rights.getPrice();
        log.info("开始远程调用service-ucenter下面的接口/KCoin/reduce,减去用户k币:" + price);
        R ucenterR = ucenterClient.reduce(price);
        if(!ucenterR.getSuccess()){
            log.error("远程调用service-ucenter下面的接口/KCoin/reduce失败，未减去用户k币");
            throw new XiaoXiaException(ResultCode.ERROR , "充值vip失败");
        }
    }



    //查找所有vip成员
    @Override
    public List<Members> findAllVipMember() {
        log.info("查询出vip所有成员");
        QueryWrapper<Members> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "user_id" , "rights_id");
        return baseMapper.selectList(wrapper);
    }

    //设置vip成员缓存,以userId为key
    @Cacheable(value = "vipMemberTreeMap")
    @Override
    public TreeMap<String , Object> findAllVipMemberTreeMap() {
        TreeMap<String , Object> treeMap = new TreeMap<>();
        List<Members> vipMemberList = findAllVipMember();
        for(Members members : vipMemberList){
            treeMap.put(members.getUserId() , members);
        }
        return treeMap;
    }


    //删除vip成员
    @Transactional
    @Override
    public void deleteMemberByIdList(List<String> idList) {
        log.info("删除vip成员,id:" + idList);
        if(idList.size() == 0){
            return;
        }
        int i = baseMapper.deleteBatchIds(idList);
        if(i != idList.size()){
            log.error("删除vip成员失败,请检查数据库");
            throw new RuntimeException();
        }
    }

    //查询用户的viplogo
    @Override
    public Map<String , Object> findMemberRightLogo(List<String> userIdList) {
        log.info("查询用户的viplogo,用户id:" + userIdList);
        //获取全部vip成员
        TreeMap<String, Object> allVipMember = findAllVipMemberTreeMap();
        //获取全部vip权益数据
        TreeMap<String, Object> allRightTreeMap = rightsService.findAllRightTreeMap();
        //获取非vip标志
        Rights rights = rightsService.findNotVipRight();
        String vipLevel = null;
        if(rights == null){
            vipLevel = "nvip";
        }else {
            vipLevel = rights.getVipLevel();
        }

        Map<String , Object> logo = new HashMap<>();
        for(String userId : userIdList){
            Object o = allVipMember.get(userId);
            if(o == null){
                //说明非vip
                logo.put(userId , vipLevel);
                continue;
            }
            //vip
            Members members = (Members) o;
            Object oRight = allRightTreeMap.get(members.getRightsId());
            if(oRight == null){
                //说明非vip
                logo.put(userId , vipLevel);
                continue;
            }
            Rights rights1 = (Rights) oRight;
            logo.put(userId , rights1.getVipLevel());
        }
        return logo;
    }

    //查询用户的vip标识
    @Override
    public String findMemberRightVipLevel(String userId) {
        Rights rights = rightsService.findRightByUserId(userId);
        if(rights == null){
            return "nvip";
        }
        return rights.getVipLevel();
    }


}
