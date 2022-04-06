package com.kuang.acl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.acl.entity.AdminUser;


public interface AdminUserService extends IService<AdminUser> {

    //通过账号查询管理员
    AdminUser getAdminByUserName(String s);
}
