package com.kuang.vip.service;

import com.kuang.vip.entity.Members;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Xiaozhang
 * @since 2022-02-07
 */
public interface MembersService extends IService<Members> {

    //用户充值vip
    void addMember(String vipId , String userId);

    //查找所有vip成员
    List<Members> findAllVipMember();

    //设置vip成员缓存,以userId为key
    TreeMap<String , Object> findAllVipMemberTreeMap();

    //删除vip成员
    void deleteMemberByIdList(List<String> idList);

    //查询用户的viplogo
    Map<String , Object> findMemberRightLogo(List<String> userIdList);

    //查询用户的vip标识
    String findMemberRightVipLevel(String userId);
}
