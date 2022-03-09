package com.kuang.vip.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author XiaoZhang
 * @date 2022/2/7 10:53
 * vip模块配置类
 */
@Configuration
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kuang.springcloud"})
@MapperScan("com.kuang.vip.mapper")
@EnableFeignClients(basePackages = {"com.kuang.vip.client"})
@EnableTransactionManagement
public class VipConfig {
}
