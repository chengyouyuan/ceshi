package com.winhxd.b2c.common.domain.system.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel("系统管理用户")
@Data
public class SysUser {

    @ApiModelProperty(value = "用户编号")
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

    @ApiModelProperty("创建时间")
    private Date created;

    @ApiModelProperty("创建人id")
    private Long createdBy;

    @ApiModelProperty("创建人")
    private String createdByName;

    @ApiModelProperty("更新人id")
    private Long updatedBy;

    @ApiModelProperty("更新时间")
    private Date updated;

    @ApiModelProperty("更新人")
    private String updatedByName;

    @ApiModelProperty(value = "角色编号")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "权限列表")
    private List<String> permissions;

}