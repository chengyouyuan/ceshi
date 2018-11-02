package com.winhxd.b2c.common.domain.system.user.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("重置密码用户名")
@Data
public class SysUserResetPasswordVerifyCondition extends ApiCondition {

    @ApiModelProperty("重置密码用户名")
    private String userAccount;

}
