package com.kuang.download;

import com.kuang.download.service.DtmCategoryService;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author XiaoZhang
 * @date 2022/2/5 16:40
 * 启动类
 */
@SpringBootApplication
public class Application implements CommandLineRunner {


    @Resource
    private DtmCategoryService dtmCategoryService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
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
    }
}
