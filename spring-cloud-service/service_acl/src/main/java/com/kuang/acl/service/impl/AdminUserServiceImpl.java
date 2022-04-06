package com.kuang.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.acl.entity.AdminUser;
import com.kuang.acl.mapper.AdminUserMapper;
import com.kuang.acl.service.AdminUserService;
import org.springframework.stereotype.Service;


@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    //通过账号查询管理员
    @Override
    public AdminUser getAdminByUserName(String s) {
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username" , s);
        return baseMapper.selectOne(wrapper);
    }
}
