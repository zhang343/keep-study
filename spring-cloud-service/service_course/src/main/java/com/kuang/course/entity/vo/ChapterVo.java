package com.kuang.course.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class ChapterVo {

    //章节id
    private String id;
    //章节标题
    private String title;
    //小节集合
    private List<VideoVo> videoList;
}
