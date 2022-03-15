package com.kuang.bbs.schedule;


import com.kuang.bbs.entity.Article;
import com.kuang.bbs.service.ArticleService;
import com.kuang.springcloud.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
/*
 * @author XiaoZhang
 * @date 2022/2/7 10:37
 * 定时任务类
 */
public class MultithreadScheduleTask {

    @Resource
    private ArticleService articleService;

    //每五分钟执行一次
    @Async
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void setArticleViews(){
        String treadName = Thread.currentThread().getName();
        log.info("开始执行定时任务,将缓存到redis中的文章浏览量同步到数据库中,当前线程名" + treadName +"当前时间:" + LocalTime.now());
        log.info(treadName + "开始尝试去获取文章全局锁");
        boolean flag = RedisUtils.tryArticleLock(360);
        if(flag){
            log.info(treadName + "获取文章全局锁成功");
            List<Article> allArticle = articleService.findAllArticle();
            List<Article> articleList = new ArrayList<>();
            for(Article article : allArticle){
                long setSize = RedisUtils.getSetSize(article.getId());
                if(setSize != 0){
                    article.setViews(article.getViews() + setSize);
                    articleList.add(article);
                    //从上面到这里删除，可能会损失一点浏览量
                    RedisUtils.delKey(article.getId());
                }
            }
            log.info(treadName + "开始去更新文章浏览量");
            if(articleList.size() != 0){
                articleService.updateBatchById(articleList);
            }
            RedisUtils.unArticleLock();
        }else {
            log.warn(treadName + "获取文章全局锁失败");
        }
    }
}
