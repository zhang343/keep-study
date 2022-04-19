package com.kuang.bbs;

import com.kuang.bbs.entity.Article;
import com.kuang.bbs.es.entity.EsArticle;
import com.kuang.bbs.es.mapper.EsArticleMapper;
import com.kuang.bbs.mapper.ArticleMapper;
import com.kuang.bbs.service.LabelService;
import com.kuang.bbs.utils.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class BBSApplication implements CommandLineRunner {

    @Resource
    private EsArticleMapper esArticleMapper;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private LabelService labelService;

    @Resource
    private ArticleMapper articleMapper;

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
        List<Article> articleList = articleMapper.selectList(null);
        List<EsArticle> esArticleList = new ArrayList<>();
        for(Article article : articleList){
            EsArticle esArticle = new EsArticle();
            BeanUtils.copyProperties(article , esArticle);
            List<String> articleLabel = labelService.findArticleLabel(article.getId());
            esArticle.setLabelList(ArrayUtils.toString(articleLabel.toArray()));
            esArticleList.add(esArticle);
        }
        esArticleMapper.saveAll(esArticleList);
    }
}
