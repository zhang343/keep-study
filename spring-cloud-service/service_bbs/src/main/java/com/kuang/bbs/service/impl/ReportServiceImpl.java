package com.kuang.bbs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.bbs.entity.Report;
import com.kuang.bbs.entity.admin.AdminReportArticleVo;
import com.kuang.bbs.mapper.ReportMapper;
import com.kuang.bbs.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    //查询是否有这条举报，通过文章id，有true，没有false
    @Override
    public boolean findReportByArticleId(String articleId) {
        QueryWrapper<Report> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        return baseMapper.selectCount(wrapper) != 0;
    }

    //举报文章接口,异步执行
    @Async
    @Override
    public void report(String articleId , String[] content) {
        //先查询是否有这一条举报
        boolean flag = findReportByArticleId(articleId);


        //如果这一条举报不存在，插入
        if(!flag){
            Report report = new Report();
            report.setArticleId(articleId);
            String contentJson = JSON.toJSONString(content);
            report.setContent(contentJson);
            baseMapper.insert(report);
        }

    }

    //查看目前的被举报的文章数量
    @Override
    public Integer findReportArticleNumber(String beginTime, String endTime) {
        QueryWrapper<Report> wrapper = new QueryWrapper<>();
        wrapper.gt("gmt_create" , beginTime);
        wrapper.lt("gmt_create" , endTime);
        return baseMapper.selectCount(wrapper);
    }

    //查看目前的被举报的文章
    @Override
    public List<AdminReportArticleVo> findReportArticle(Long current, Long limit, String beginTime, String endTime) {
        current = (current - 1) * limit;
        return baseMapper.findReportArticle(current , limit , beginTime , endTime);
    }
}
