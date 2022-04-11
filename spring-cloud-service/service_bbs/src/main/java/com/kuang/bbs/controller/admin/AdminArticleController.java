package com.kuang.bbs.controller.admin;

import com.kuang.bbs.entity.admin.AdminReportArticleVo;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.ReportService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/bbs/abc")
@Slf4j
public class AdminArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ReportService reportService;

    //设置置顶文章
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("setTopArticle")
    public R setTopArticle(String articleId){
        if(StringUtils.isEmpty(articleId)){
            throw new XiaoXiaException(ResultCode.ERROR , "The article does not exist");
        }
        articleService.setTopArticle(articleId);
        return R.ok();
    }

    //查看目前的被举报的文章，按创建时间排序
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("findReportArticle")
    public R findReportArticle(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                               @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                               String beginTime , String endTime){
        Integer total = reportService.findReportArticleNumber(beginTime , endTime);
        List<AdminReportArticleVo> articleVoList = reportService.findReportArticle(current , limit , beginTime , endTime);
        return R.ok().data("articleList" , articleVoList).data("total" , total);
    }

    //删除举报
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("deleteReport")
    public R deleteReport(String reportId){
        boolean b = reportService.removeById(reportId);
        if(!b){
            throw new XiaoXiaException(ResultCode.ERROR , " Failed to delete Report");
        }
        return R.ok();
    }


    //封禁该文章
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("banArticle")
    public R banArticle(String reportId , String articleId){
        articleService.banArticle(reportId , articleId);
        return R.ok();
    }


}
