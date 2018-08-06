package com.winhxd.b2c.common.domain.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel("系统管理用户编辑传输对象")
@Data
public class SysUserDTO {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "姓名")
    private String username;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "状态（0：未启用，1：启用）")
    private Short status;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "角色编号")
    private Long ruleId;

}