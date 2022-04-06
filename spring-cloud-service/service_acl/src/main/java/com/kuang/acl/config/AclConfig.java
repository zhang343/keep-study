package com.kuang.acl.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kuang.springcloud"})
@MapperScan("com.kuang.acl.mapper")
@EnableTransactionManagement
public class AclConfig {
}
