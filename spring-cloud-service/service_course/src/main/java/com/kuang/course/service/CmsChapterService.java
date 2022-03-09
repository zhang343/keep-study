package com.kuang.course.service;

import com.kuang.course.entity.CmsChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.course.entity.vo.ChapterVo;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
public interface CmsChapterService extends IService<CmsChapter> {

    //查询章节和小节,通过课程id
    List<ChapterVo> findChapterByCourseId(String courseId);
}
