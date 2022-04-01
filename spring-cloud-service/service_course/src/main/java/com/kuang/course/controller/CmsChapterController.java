package com.kuang.course.controller;


import com.kuang.course.entity.vo.ChapterVo;
import com.kuang.course.service.CmsChapterService;
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


@RestController
@RequestMapping("/cms/chapter")
@Slf4j
public class CmsChapterController {

    @Resource
    private CmsChapterService chapterService;

    //查询课程下面的章节和小节
    @GetMapping("findChapterAndVideo")
    public R findChapterAndVideo(String courseId){
        //校验数据
        if(StringUtils.isEmpty(courseId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        //查询章节和小节
        List<ChapterVo> chapterVoList = chapterService.findChapterByCourseId(courseId);
        return R.ok().data("chapterList" , chapterVoList);
    }
}

