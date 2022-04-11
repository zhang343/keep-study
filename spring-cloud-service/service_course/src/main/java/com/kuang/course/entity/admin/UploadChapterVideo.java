package com.kuang.course.entity.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UploadChapterVideo {

    private String courseId;
    private String chapterId;
    private Integer sort;
    private String title;
    private Integer length;
    private String videoSourceId;
    private String videoOriginalName;
}
