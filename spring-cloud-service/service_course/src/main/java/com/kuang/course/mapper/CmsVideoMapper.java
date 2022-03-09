package com.kuang.course.mapper;

import com.kuang.course.entity.CmsVideo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.course.entity.vo.VideoVo;
import com.kuang.springcloud.entity.UserStudyVo;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
public interface CmsVideoMapper extends BaseMapper<CmsVideo> {

    //查询视频，通过章节id
    List<VideoVo> findVideoByChapterId(String chapterId);

    //查询视频云端id和相关课程信息
    UserStudyVo findUserStudyVoByCourseId(String id);
}
