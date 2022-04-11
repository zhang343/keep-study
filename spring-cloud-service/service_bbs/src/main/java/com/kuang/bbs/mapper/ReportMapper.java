package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.Report;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.bbs.entity.admin.AdminReportArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface ReportMapper extends BaseMapper<Report> {

    //查看目前的被举报的文章
    List<AdminReportArticleVo> findReportArticle(@Param("current") Long current,
                                                 @Param("limit") Long limit,
                                                 @Param("beginTime") String beginTime,
                                                 @Param("endTime") String endTime);
}
