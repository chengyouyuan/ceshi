package com.winhxd.b2c.common.domain.system.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 管理后台用户对象
 */
@Data
public class UserInfo {

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

    @ApiModelProperty(value = "角色编号")
    private Long ruleId;

    @ApiModelProperty(value = "角色名称")
    private String ruleName;

    @ApiModelProperty(value = "权限列表")
    private List<String> permissions;

}
