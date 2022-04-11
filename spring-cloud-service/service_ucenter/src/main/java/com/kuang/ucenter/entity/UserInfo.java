package com.kuang.ucenter.entity;

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
 * @since 2022-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserInfo对象", description="会员表")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "电话号码")
    private String phoneNumber;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "性别 0 女，1 男")
    private Boolean sex;

    @ApiModelProperty(value = "用户等级经验")
    private Integer experience;

    @ApiModelProperty(value = "用户签名")
    private String sign;

    @ApiModelProperty(value = "用户背景图片")
    private String bgImg;

    @ApiModelProperty(value = "用户k币数量")
    private Integer money;

    @ApiModelProperty(value = "用户地址")
    private String address;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "是否禁用 0（false）未禁用，  1（true）禁用")
    private Boolean isDisabled;

    @ApiModelProperty(value = "0未签到 1签到")
    private Boolean isSign;

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
