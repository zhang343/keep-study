package com.kuang.bbs;

import com.kuang.bbs.entity.Comment;
import com.kuang.bbs.entity.vo.IndexArticleVo;
import com.kuang.bbs.mapper.CommentMapper;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.CommentService;
import com.kuang.springcloud.utils.RedisUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author XiaoZhang
 * @date 2022/2/8 9:37
 * 启动类
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Resource
    private ArticleService articleService;

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
