package com.winhxd.b2c.common.domain.system.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel("系统管理用户")
@Data
public class SysUser {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "状态（0：未启用，1：启用）")
    private Short status;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private Date created;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private Date updated;

    @ApiModelProperty(value = "角色编号")
    private Long ruleId;

    @ApiModelProperty(value = "角色名称")
    private String ruleName;

    @ApiModelProperty(value = "权限列表")
    private List<String> permissions;

}