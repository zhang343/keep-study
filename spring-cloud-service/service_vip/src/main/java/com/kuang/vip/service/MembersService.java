package com.kuang.vip.service;

import com.kuang.vip.entity.Members;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface MembersService extends IService<Members> {

    //用户充值vip
    String addMember(String vipId , String userId);

    //查询用户的viplogo
    Object findUserVipLevel(List<String> userIdList);
}
