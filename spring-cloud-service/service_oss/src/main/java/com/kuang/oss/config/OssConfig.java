package com.kuang.oss.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author XiaoZhang
 * @date 2022/2/3 12:31
 * Oss模块配置类
 */
@Configuration
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kuang.springcloud"})
@EnableFeignClients(basePackages = {"com.kuang.oss.client"})
public class OssConfig {
}
