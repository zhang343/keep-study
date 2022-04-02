package com.kuang.springcloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InfoMyNewsVo {

    private String userId;
    private String content;
    private Boolean isCourse;
    private String title;
    private String courseId;
}
