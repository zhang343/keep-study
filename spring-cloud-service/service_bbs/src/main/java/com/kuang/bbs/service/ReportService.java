package com.kuang.bbs.service;

import com.kuang.bbs.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.admin.AdminReportArticleVo;

import java.util.List;


public interface ReportService extends IService<Report> {

    //查询是否有这条举报，通过文章id，有true，没有false
    boolean findReportByArticleId(String articleId);

    //举报文章接口,异步执行
    void report(String articleId , String[] content);

    //查看目前的被举报的文章数量
    Integer findReportArticleNumber(String beginTime, String endTime);

    //查看目前的被举报的文章
    List<AdminReportArticleVo> findReportArticle(Long current, Long limit, String beginTime, String endTime);
}
