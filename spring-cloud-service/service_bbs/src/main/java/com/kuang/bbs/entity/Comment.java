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
@TableName("bbs_comment")
@ApiModel(value="Comment对象", description="")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "文章id")
    private String articleId;

    @ApiModelProperty(value = "用户1id")
    private String userId;

    @ApiModelProperty(value = "用户1头像")
    private String userAvatar;

    @ApiModelProperty(value = "用户1昵称")
    private String userNickname;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "父级id，0表示一级评论，非0为二级评论")
    private String fatherId;

    @ApiModelProperty(value = "回复用户id")
    private String replyUserId;

    @ApiModelProperty(value = "回复用户昵称")
    private String replyUserNickname;

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
