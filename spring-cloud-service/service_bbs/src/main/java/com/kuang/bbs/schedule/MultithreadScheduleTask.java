package com.kuang.bbs.schedule;


import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.Column;
import com.kuang.bbs.mapper.ArticleRightMapper;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.ColumnService;
import com.kuang.springcloud.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Resource
    private ArticleRightMapper articleRightMapper;

    @Resource
    private ColumnService columnService;

    //每五分钟执行一次
    @Async
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void setArticleViews(){
        String treadName = Thread.currentThread().getName();
        log.info("开始执行定时任务,将缓存到redis中的文章浏览量同步到数据库中,当前线程名" + treadName +"当前时间:" + LocalTime.now());
        log.info(treadName + "开始尝试去获取文章全局锁");
        //这里设置为4分钟
        boolean flag = RedisUtils.tryArticleLock(240);
        if(flag){
            log.info(treadName + "获取文章全局锁成功");
            //获取这段时间被访问的文章
            Set<Object> setAll = RedisUtils.getSetAll(RedisUtils.ARTICLLE);
            if(setAll == null || setAll.size() == 0){
                RedisUtils.unArticleLock();
                return;
            }
            RedisUtils.delKey(RedisUtils.ARTICLLE);
            List<String> articleIdList = new ArrayList<>();
            for(Object o : setAll){
                articleIdList.add((String) o);
            }

            //查询出文章浏览量
            List<Article> articleList = articleService.findArticleViewsList(articleIdList);
            List<Article> articleUpdateList = new ArrayList<>();
            for(Article article : articleList){
                long setSize = RedisUtils.getSetSize(article.getId());
                RedisUtils.delKey(article.getId());
                if(setSize != 0){
                    article.setViews(article.getViews() + setSize);
                    articleUpdateList.add(article);
                }
            }

            log.info(treadName + "开始去更新文章浏览量");
            if(articleUpdateList.size() != 0){
                articleService.updateArticleViews(articleUpdateList);
            }

            RedisUtils.unArticleLock();
        }else {
            log.warn(treadName + "获取文章全局锁失败");
        }
    }


    //每五分钟执行一次
    @Async
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void setColumnViews(){
        String treadName = Thread.currentThread().getName();
        log.info("开始执行定时任务,将缓存到redis中的专栏浏览量同步到数据库中,当前线程名" + treadName +"当前时间:" + LocalTime.now());
        log.info(treadName + "开始尝试去获取专栏全局锁");


        //这里设置为4分钟
        boolean flag = RedisUtils.tryColumnLock(240);
        if(flag){
            log.info(treadName + "获取专栏全局锁成功");
            //获取这段时间被访问的专栏
            Set<Object> setAll = RedisUtils.getSetAll(RedisUtils.COLUMN);
            if(setAll == null || setAll.size() == 0){
                RedisUtils.unColumnLock();
                return;
            }

            RedisUtils.delKey(RedisUtils.COLUMN);
            List<String> columnIdList = new ArrayList<>();
            for(Object o : setAll){
                columnIdList.add((String) o);
            }

            //查询出专栏浏览量
            List<Column> columnList = columnService.findColumnViewsList(columnIdList);
            List<Column> columnUpdateList = new ArrayList<>();
            for(Column column : columnList){
                long setSize = RedisUtils.getSetSize(column.getId());
                RedisUtils.delKey(column.getId());
                if(setSize != 0){
                    column.setViews(column.getViews() + setSize);
                    columnUpdateList.add(column);
                }
            }

            log.info(treadName + "开始去更新专栏浏览量");
            if(columnUpdateList.size() != 0){
                columnService.updateColumnViews(columnUpdateList);
            }

            RedisUtils.unColumnLock();
        }else {
            log.warn(treadName + "获取专栏全局锁失败");
        }
    }


    //定时任务,更新用户每日文章权益，每天凌晨执行
    @Scheduled(cron = "0 0 0 * * ? ")
    public void updateUserArticleRight(){
        log.info("开始执行定时任务,更新用户每日文章权益,当前时间为:" + LocalDateTime.now());
        articleRightMapper.updateUserArticleRight();
    }
}
