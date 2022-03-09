package com.kuang.course.service.impl;

import com.kuang.course.entity.CmsChapter;
import com.kuang.course.entity.vo.ChapterVo;
import com.kuang.course.mapper.CmsChapterMapper;
import com.kuang.course.service.CmsChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
@Service
@Slf4j
public class CmsChapterServiceImpl extends ServiceImpl<CmsChapterMapper, CmsChapter> implements CmsChapterService {

    //查询章节和小节,通过课程id
    @Override
    public List<ChapterVo> findChapterByCourseId(String courseId) {
        log.info("通过课程id来查找章节,课程id:" + courseId);
        return baseMapper.findChapterByCourseId(courseId);
    }
}
