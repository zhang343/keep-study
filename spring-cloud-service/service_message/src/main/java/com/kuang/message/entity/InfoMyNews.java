package com.kuang.message.entity;

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
 * @since 2022-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="InfoMyNews对象", description="")
public class InfoMyNews implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "用户id（该消息属于那个用户)")
    private String userId;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "是不是课程通知，0不是，1是")
    private Boolean isCourse;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "主键id")
    private String courseId;

    @ApiModelProperty(value = "是否阅读 0 未阅读 1阅读")
    private Boolean isRead;

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
