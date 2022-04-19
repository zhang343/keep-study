package com.kuang.springcloud.utils;



public interface ResultCode {
    //响应成功状态码
    Integer SUCCESS = 20000;
    //普通响应失败状态码
    Integer ERROR = 20001;
    //账号异常状态码
    Integer ACCOUNTERROR = 20002;
    //密码异常状态码
    Integer PASSWORDERROR = 20003;
    //网关异常
    Integer GATEWAYERROR = 20004;
}