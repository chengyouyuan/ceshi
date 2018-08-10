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

    @ApiModelProperty("组内人数")
    private int userCount;

    @ApiModelProperty("权限范围")
    private List<SysRolePermission> permissions;

}