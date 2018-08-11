package com.winhxd.b2c.common.domain.system.user.condition;

import com.winhxd.b2c.common.domain.common.BaseCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangzhengyang
 * @description 系统用户condition
 * @date 2018/8/1
 */
@ApiModel("系统用户列表查询参数")
@Data
public class SysUserCondition extends BaseCondition {

    @ApiModelProperty(value = "编号")
    private Long userId;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "姓名")
    private String username;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

}
