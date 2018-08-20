package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayStoreWalletCondition extends ApiCondition{
	private Long id;
    private Long storeId;
	@ApiModelProperty("微信账号")
	private String openid;
	@ApiModelProperty("昵称")
    private String nick;
	@ApiModelProperty("用户实名")
    private String name;
	@ApiModelProperty("手机号")
    private String mobile;
	@ApiModelProperty("验证码")
    private String verificationCode;
}
