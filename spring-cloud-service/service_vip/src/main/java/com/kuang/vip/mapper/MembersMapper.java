package com.kuang.vip.mapper;

import com.kuang.springcloud.entity.MembersRedis;
import com.kuang.vip.entity.Members;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


public interface MembersMapper extends BaseMapper<Members> {

    //查询出所有vip成员
    List<MembersRedis> findAllMembers();
}
