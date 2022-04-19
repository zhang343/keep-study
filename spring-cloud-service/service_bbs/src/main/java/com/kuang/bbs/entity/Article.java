package com.kuang.bbs.entity;

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
@TableName("bbs_article")
@ApiModel(value="Article对象", description="")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "文章分类id")
    private String categoryId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章描述")
    private String description;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "排序字段")
    private Long sort;

    @ApiModelProperty(value = "0不是专栏文章  1是专栏文章")
    private Boolean isColumnArticle;

    @ApiModelProperty(value = "0未发布  1发布")
    private Boolean isRelease;

    @ApiModelProperty(value = "0未同步到江湖  1同步")
    private Boolean isBbs;

    @ApiModelProperty(value = "浏览量")
    private Long views;

    @ApiModelProperty(value = "0未违规 1违规")
    private Boolean isViolationArticle;

    @ApiModelProperty(value = "是否为精品文章 0不是 1是")
    private Boolean isExcellentArticle;

    @ApiModelProperty(value = "是不是置顶，0不置，1置")
    private Boolean isTop;

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
