package com.kuang.vip;

import com.kuang.vip.service.CacheService;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;


@SpringBootApplication
public class VipApplication implements CommandLineRunner {

    @Resource
    private CacheService cacheService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(VipApplication.class);
        springApplication.setBannerMode(Banner.Mode.CONSOLE);
        springApplication.setAllowBeanDefinitionOverriding(false);
        springApplication.setLazyInitialization(false);
        springApplication.setLogStartupInfo(true);
        springApplication.setRegisterShutdownHook(true);
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);
        springApplication.run(args);
    }

    //做程序启动之后工作
    @Override
    public void run(String... args) throws Exception {
        System.out.println(cacheService.CacheNotVipRight());
    }
}
