package com.kuang.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.Report;
import com.kuang.bbs.mapper.ArticleMapper;
import com.kuang.bbs.mapper.ReportMapper;
import com.kuang.bbs.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Service
@Slf4j
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Resource
    private ArticleMapper articleMapper;

    //查询是否有这条举报，通过文章id，有true，没有false
    @Override
    public boolean findReportByArticleId(String articleId) {
        log.info("查询是否有这条举报,通过文章id,有true,没有false,文章id:" + articleId);
        QueryWrapper<Report> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        return baseMapper.selectCount(wrapper) != 0;
    }

    //举报文章接口,异步执行
    @Async
    @Override
    public void report(String articleId , String content) {
        log.info("举报文章,文章id:" + articleId + ",举报内容:" + content);
        boolean flag = findReportByArticleId(articleId);
        //已经有人举报，不插入
        if(flag){
            return;
        }
        Article article = articleMapper.selectById(articleId);
        //文章不存在或处于封禁状态不可以举报
        if(article == null || article.getIsViolationArticle()){
            return;
        }

        //文章没有发布，不可以举报
        if(!article.getIsColumnArticle() && !article.getIsRelease()){
            return;
        }

        //文章是专栏文章，但没有同步到江湖，不可以举报
        if(article.getIsColumnArticle() && !article.getIsBbs()){
            return;
        }

        Report report = new Report();
        report.setArticleId(articleId);
        report.setContent(content);
        baseMapper.insert(report);
    }
}
