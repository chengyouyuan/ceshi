package com.winhxd.b2c.common.domain.system.user.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("重置密码入参")
@Data
public class SysUserResetPasswordCondition extends ApiCondition {

    @ApiModelProperty("用户名")
    private String userAccount;
    @ApiModelProperty("取回密码方式")
    private String retrieveWay;
    @ApiModelProperty("短信验证码")
    private String verifyCode;
    @ApiModelProperty("密码")
    private String pwd;
    @ApiModelProperty("确认密码")
    private String repwd;

}
