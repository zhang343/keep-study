package com.kuang.course.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CmsDetails对象", description="")
public class CmsDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程详情表id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "课程概述")
    private String overview;

    @ApiModelProperty(value = "讲师介绍")
    private String teacher;

    @ApiModelProperty(value = "适合人群")
    private String suitablePeople;

    @ApiModelProperty(value = "课程概述")
    private String courseArrange;

    @ApiModelProperty(value = "课程反馈")
    private String courseFeedback;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Long version;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
