package com.kuang.course.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.annotation.EnableCaching;
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
 * 课程模块配置类
 */
@Configuration
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kuang.springcloud"})
@MapperScan("com.kuang.course.mapper")
@EnableFeignClients(basePackages = {"com.kuang.course.client"})
@EnableTransactionManagement
public class CourseConfig {
}
