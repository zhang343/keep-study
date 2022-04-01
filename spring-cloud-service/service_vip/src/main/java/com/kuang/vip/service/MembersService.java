package com.kuang.vip.service;

import com.kuang.vip.entity.Members;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface MembersService extends IService<Members> {

    //用户充值vip
    void addMember(String vipId , String userId);

    //查询用户的viplogo
    Object findUserVipLevel(List<String> userIdList);
}
