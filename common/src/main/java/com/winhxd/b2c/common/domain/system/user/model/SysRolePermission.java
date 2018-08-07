package com.winhxd.b2c.common.domain.system.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统角色关联权限")
@Data
public class SysRolePermission {

    @ApiModelProperty("角色权限关联编号")
    private Long id;

    @ApiModelProperty("权组编号")
    private Long ruleId;

    @ApiModelProperty("权限编码")
    private String permission;

    @ApiModelProperty("权限名称")
    private String permissionName;

}