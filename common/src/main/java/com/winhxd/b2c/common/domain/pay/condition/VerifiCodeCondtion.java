package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("短信验证码入参")
@Data
public class VerifiCodeCondtion extends ApiCondition{
	@ApiModelProperty("手机号")
	private String mobile;
	@ApiModelProperty("提现类型 1微信2银行卡")
	private short withdrawType;

}
