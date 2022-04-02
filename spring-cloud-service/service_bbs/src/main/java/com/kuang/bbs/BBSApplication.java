package com.kuang.bbs;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BBSApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BBSApplication.class);
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
