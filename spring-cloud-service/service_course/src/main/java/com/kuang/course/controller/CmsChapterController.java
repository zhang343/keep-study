package com.kuang.course.controller;


import com.kuang.course.entity.vo.ChapterVo;
import com.kuang.course.entity.vo.VideoVo;
import com.kuang.course.service.CmsChapterService;
import com.kuang.course.service.CmsVideoService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 * 课程章节处理类
 */
@RestController
@RequestMapping("/cms/chapter")
@Slf4j
public class CmsChapterController {

    @Resource
    private CmsChapterService chapterService;

    //查询课程下面的章节和小节
    @GetMapping("findChapterAndVideo")
    public R findChapterAndVideo(String courseId){
        log.info("开始查询课程章节和小节,课程id:" + courseId);
        if(StringUtils.isEmpty(courseId)){
            log.warn("有人非法进行非法操作查询课程下面的章节和小节,课程id:" + courseId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        //查询章节和小节
        List<ChapterVo> chapterVoList = chapterService.findChapterByCourseId(courseId);
        return R.ok().data("chapterList" , chapterVoList);
    }
}

