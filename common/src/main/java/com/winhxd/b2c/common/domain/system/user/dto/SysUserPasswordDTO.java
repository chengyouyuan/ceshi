package com.winhxd.b2c.common.domain.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangzhengyang
 * @description 系统用户DTO
 * @date 2018/8/1
 */
@ApiModel("修改系统用户密码参数")
@Data
public class SysUserPasswordDTO {

    @ApiModelProperty(value = "用户编号", required = true)
    private Long id;

    @ApiModelProperty(value = "原密码", required = true)
    private String password;

    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;

    @ApiModelProperty("更新人id")
    private Long updatedBy;

    @ApiModelProperty("更新时间")
    private Date updated;

    @ApiModelProperty("更新人")
    private String updatedByName;

}
