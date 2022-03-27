package com.kuang.bbs.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Xiaozhang
 * @since 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bbs_column")
@ApiModel(value="Column对象", description="")
public class Column implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户专栏id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "专栏名称")
    private String title;

    @ApiModelProperty(value = "浏览数")
    private Long views;

    @ApiModelProperty(value = "可见度")
    private Long vsibility;

    @ApiModelProperty(value = "0 不发布 1 发布，这里指专栏是否可被别人看见")
    private Boolean isRelease;

    @ApiModelProperty(value = "专栏描述")
    private String description;

    @ApiModelProperty(value = "专栏渐变色")
    private String color;

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
