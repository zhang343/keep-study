package com.kuang.acl.service.impl;


import com.kuang.acl.entity.AdminUser;
import com.kuang.acl.service.AdminUserService;
import com.kuang.springcloud.entity.security.JwtUser;
import com.kuang.springcloud.entity.security.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 具体处理用户认证业务逻辑
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private AdminUserService adminUserService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AdminUser AdminUser = adminUserService.getAdminByUserName(s);
        if(AdminUser == null){
            throw new UsernameNotFoundException("当前账号尚未存在");
        }
        User user = new User();
        BeanUtils.copyProperties(AdminUser , user);
        return new JwtUser(user);
    }
}
