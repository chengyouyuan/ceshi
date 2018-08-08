package com.winhxd.b2c.common.domain.system.login.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("小程序用户换绑手机号请求参数")
@Data
public class CustomerChangeMobileCondition {
	@ApiModelProperty(value = "换绑账号")
	private String customerMobile;
}
