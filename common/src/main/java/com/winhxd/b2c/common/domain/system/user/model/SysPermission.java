package com.winhxd.b2c.common.domain.system.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("系统权限")
@Data
public class SysPermission {

    @ApiModelProperty(value = "权限编码", required = true)
    private String permission;

    @ApiModelProperty(value = "权限名称", required = true)
    private String permissionName;

    @ApiModelProperty(value = "子权限", required = true)
    private List<SysPermission> children;

    @JsonIgnore
    private SysPermission parent;

}