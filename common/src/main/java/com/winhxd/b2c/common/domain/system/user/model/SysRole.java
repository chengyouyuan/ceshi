package com.winhxd.b2c.common.domain.system.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel("系统管理权组")
@Data
public class SysRole {

    @ApiModelProperty("权组编号")
    private Long id;

    @ApiModelProperty("权组名称")
    private String roleName;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private Date created;

    @ApiModelProperty("修改人")
    private String updatedBy;

    @ApiModelProperty("修改时间")
    private Date updated;

    @ApiModelProperty("权限范围")
    private List<SysRolePermission> permissions;

}