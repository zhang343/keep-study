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
@ApiModel(value="CmsCourse对象", description="")
public class CmsCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "一级分类id")
    private String ocId;

    @ApiModelProperty(value = "二级分类id")
    private String tcId;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程描述")
    private String description;

    @ApiModelProperty(value = "课程封面地址")
    private String cover;

    @ApiModelProperty(value = "课程的总时长(以秒为单位)")
    private Integer totalLength;

    @ApiModelProperty(value = "课程状态 Draft未更新完毕  Normal更新完毕")
    private String status;

    @ApiModelProperty(value = "播放数量")
    private Long views;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private Integer price;

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
