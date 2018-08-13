package com.winhxd.b2c.common.domain.system.login.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("小程序用户换绑手机号请求参数")
@Data
public class CustomerChangeMobileCondition  extends ApiCondition{
	@ApiModelProperty(value = "换绑账号")
	private String customerMobile;
	@ApiModelProperty(value = "短信验证码")
	private String verificationCode;
}
