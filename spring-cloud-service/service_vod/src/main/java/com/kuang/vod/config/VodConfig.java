package com.kuang.vod.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author XiaoZhang
 * @date 2022/2/10 12:31
 * Vod模块配置类
 */
@Configuration
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kuang.springcloud"})
@EnableFeignClients(basePackages = {"com.kuang.vod.client"})
public class VodConfig {
}
