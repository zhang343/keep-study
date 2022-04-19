package com.kuang.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.course.entity.CmsVideo;
import com.kuang.course.entity.vo.VideoVo;

import java.util.List;


public interface CmsVideoMapper extends BaseMapper<CmsVideo> {

    //查询视频，通过章节id
    List<VideoVo> findVideoByChapterId(String chapterId);
}
