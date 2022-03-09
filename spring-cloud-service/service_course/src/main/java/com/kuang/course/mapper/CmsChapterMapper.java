package com.kuang.course.mapper;

import com.kuang.course.entity.CmsChapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.course.entity.vo.ChapterVo;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
public interface CmsChapterMapper extends BaseMapper<CmsChapter> {

    //查询章节通过课程id
    List<ChapterVo> findChapterByCourseId(String courseId);
}
