package com.kuang.course.controller.admin;

import com.kuang.course.entity.CmsChapter;
import com.kuang.course.entity.CmsCourse;
import com.kuang.course.entity.CmsDetails;
import com.kuang.course.entity.CmsVideo;
import com.kuang.course.entity.admin.UploadChapterVideo;
import com.kuang.course.entity.admin.UploadCourseChapter;
import com.kuang.course.entity.admin.UploadCourseDetailsVo;
import com.kuang.course.entity.admin.UploadCourseVo;
import com.kuang.course.service.CmsChapterService;
import com.kuang.course.service.CmsCourseService;
import com.kuang.course.service.CmsDetailsService;
import com.kuang.course.service.CmsVideoService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cms/admin")
@Slf4j
public class AdminCourseController {

    @Resource
    private CmsCourseService courseService;

    @Resource
    private CmsDetailsService detailsService;

    @Resource
    private CmsChapterService chapterService;

    @Resource
    private CmsVideoService videoService;

    //上传课程
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("uploadCourse")
    public R uploadCourse(UploadCourseVo uploadCourseVo){
        if(StringUtils.isEmpty(uploadCourseVo.getOcId()) ||
                StringUtils.isEmpty(uploadCourseVo.getTcId()) ||
                (uploadCourseVo.getSort() < 0) ||
                StringUtils.isEmpty(uploadCourseVo.getTitle()) ||
                StringUtils.isEmpty(uploadCourseVo.getDescription()) ||
                StringUtils.isEmpty(uploadCourseVo.getCover()) ||
                (!uploadCourseVo.getStatus().equals("Draft") && !uploadCourseVo.getStatus().equals("Normal")) ||
                (uploadCourseVo.getPrice() < 0)){
            throw new XiaoXiaException(ResultCode.ERROR , "课程信息不完善，请完善");
        }
        CmsCourse course = new CmsCourse();
        BeanUtils.copyProperties(uploadCourseVo , course);
        boolean save = courseService.save(course);
        if(!save){
            throw new XiaoXiaException(ResultCode.ERROR , "创建课程失败");
        }
        return R.ok().data("courseId" , course.getId());
    }

    //创建课程详情
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("uploadCourseDetail")
    public R uploadCourseDetail(UploadCourseDetailsVo uploadCourseDetailsVo){
        if(StringUtils.isEmpty(uploadCourseDetailsVo.getCourseId())){
            throw new XiaoXiaException(ResultCode.ERROR , "课程详情信息不完善，请完善");
        }
        CmsDetails details = new CmsDetails();
        BeanUtils.copyProperties(uploadCourseDetailsVo , details);
        boolean save = detailsService.save(details);
        if(!save){
            throw new XiaoXiaException(ResultCode.ERROR , "创建课程详情失败");
        }
        return R.ok().data("courseDetailId" , details.getId());
    }

    //创建课程章节
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("uploadCourseChapter")
    public R uploadCourseChapter(UploadCourseChapter uploadCourseChapter){
        if(StringUtils.isEmpty(uploadCourseChapter.getCourseId()) ||
                StringUtils.isEmpty(uploadCourseChapter.getTitle()) ||
                uploadCourseChapter.getSort() < 0){
            throw new XiaoXiaException(ResultCode.ERROR , "章节信息不完善，请完善");
        }

        CmsChapter chapter = new CmsChapter();
        BeanUtils.copyProperties(uploadCourseChapter , chapter);
        boolean save = chapterService.save(chapter);
        if(!save){
            throw new XiaoXiaException(ResultCode.ERROR , "创建课程章节失败");
        }

        return R.ok().data("chapterId" , chapter.getId());

    }

    //上传小节
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("uploadChapterVideo")
    public R uploadChapterVideo(UploadChapterVideo uploadChapterVideo){
        if(StringUtils.isEmpty(uploadChapterVideo.getCourseId()) ||
                StringUtils.isEmpty(uploadChapterVideo.getChapterId()) ||
                (uploadChapterVideo.getSort() < 0) ||
                StringUtils.isEmpty(uploadChapterVideo.getTitle()) ||
                (uploadChapterVideo.getLength() <= 0) ||
                StringUtils.isEmpty(uploadChapterVideo.getVideoSourceId()) ||
                StringUtils.isEmpty(uploadChapterVideo.getVideoOriginalName())){
            throw new XiaoXiaException(ResultCode.ERROR , "小节信息不完善，请完善");
        }

        CmsVideo video = new CmsVideo();
        BeanUtils.copyProperties(uploadChapterVideo , video);
        videoService.uploadChapterVideo(video);
        return R.ok().data("videoId" , video.getId());
    }

    //删除小节
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("deleteVideo")
    public R deleteVideo(String videoId , HttpServletRequest request){
        videoService.deleteVideo(videoId , request.getHeader("token"));
        return R.ok();
    }

}
