package com.kuang.bbs.config;

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
 * @date 2022/2/8 10:53
 * 江湖模块配置类
 */
@Configuration
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kuang.springcloud"})
@MapperScan("com.kuang.bbs.mapper")
@EnableFeignClients(basePackages = {"com.kuang.bbs.client"})
@EnableTransactionManagement
public class CourseConfig {
}
