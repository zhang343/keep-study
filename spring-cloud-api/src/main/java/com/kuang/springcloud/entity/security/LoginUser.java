package com.kuang.springcloud.entity.security;

import lombok.Data;

//登录的存储账号和密码
@Data
public class LoginUser {
    private String username;
    private String password;
}
