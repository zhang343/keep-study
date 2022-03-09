package com.kuang.vip.entity;

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
 * @since 2022-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vip_rights")
@ApiModel(value="Rights对象", description="")
public class Rights implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "vip标识")
    private String vipLevel;

    @ApiModelProperty(value = "价格")
    private Integer price;

    @ApiModelProperty(value = "课程打折多少")
    private Double courseDiscount;

    @ApiModelProperty(value = "专栏数量")
    private Integer columnNumber;

    @ApiModelProperty(value = "每日签到经验")
    private Integer signExperience;

    @ApiModelProperty(value = "每日k币上限")
    private Integer money;

    @ApiModelProperty(value = "每日发帖数量，-1表示无限")
    private Integer articleNumber;

    @ApiModelProperty(value = "vip时长")
    private Integer timeLength;

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
