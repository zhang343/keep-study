package com.kuang.bbs.service;

import com.kuang.bbs.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface ReportService extends IService<Report> {

    //查询是否有这条举报，通过文章id，有true，没有false
    boolean findReportByArticleId(String articleId);

    //举报文章接口,异步执行
    void report(String articleId , String content);
}
