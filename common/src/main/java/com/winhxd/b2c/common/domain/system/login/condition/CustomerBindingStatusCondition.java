package com.winhxd.b2c.common.domain.system.login.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户请求参数", description = "后台用户解绑和换绑请求参数")
public class CustomerBindingStatusCondition {

    @ApiModelProperty("门店id")
    private Long storeId;
    @ApiModelProperty("用户id")
    private Long customerId;
}
