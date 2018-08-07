package com.winhxd.b2c.common.domain.system.user.dto;

import com.winhxd.b2c.common.domain.system.user.model.SysRolePermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("系统管理权组传输对象")
@Data
public class SysRoleDTO {

    @ApiModelProperty("权组编号")
    private Long id;

    @ApiModelProperty("权组名称")
    private String roleName;

    @ApiModelProperty("权限范围")
    private List<SysRolePermission> permissions;

}