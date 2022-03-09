package com.kuang.download.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author XiaoZhang
 * @date 2022/2/5 16:40
 * 下载模块配置类
 */
@Configuration
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kuang.springcloud"})
@MapperScan("com.kuang.download.mapper")
@EnableTransactionManagement
public class DownloadConfig {

}
