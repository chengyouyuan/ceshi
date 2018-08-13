package com.winhxd.b2c.common.domain.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统管理用户登录传输对象")
@Data
public class SysUserLoginDTO {

    @ApiModelProperty(value = "账号", required = true)
    private String account;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

}