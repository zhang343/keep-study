package com.kuang.message.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author XiaoZhang
 * @date 2022/2/11 10:53
 * 消息模块配置类
 */
@Configuration
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kuang.springcloud"})
@MapperScan("com.kuang.message.mapper")
@EnableFeignClients(basePackages = {"com.kuang.message.client"})
@EnableTransactionManagement
public class MessageConfig {
}
