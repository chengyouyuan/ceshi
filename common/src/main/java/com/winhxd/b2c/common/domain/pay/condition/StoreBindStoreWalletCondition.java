package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StoreBindStoreWalletCondition extends ApiCondition {

	@ApiModelProperty("openid")
	private String openid;
	@ApiModelProperty("用户实名")
	private String name;
	@ApiModelProperty("昵称")
	private String nick;
	
}
