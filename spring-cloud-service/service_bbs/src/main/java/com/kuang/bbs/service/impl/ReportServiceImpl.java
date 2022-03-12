package com.kuang.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.bbs.entity.Report;
import com.kuang.bbs.mapper.ReportMapper;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.ReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    private ArticleService articleService;

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
        boolean isColumnArticle = articleService.findIsColumnArticle(articleId);
        if(!flag && !isColumnArticle){
            Report report = new Report();
            report.setArticleId(articleId);
            report.setContent(content);
            baseMapper.insert(report);
        }
    }
}
