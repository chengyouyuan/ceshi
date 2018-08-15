package com.winhxd.b2c.common.domain.system.login.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author wufuyun
 * @date  2018年8月9日 下午12:28:47
 * @Description B端发送验证码
 * @version
 */
@ApiModel("惠小店用户发送验证码求参数")
@Data
public class StoreSendVerificationCodeCondition extends ApiCondition{
	@ApiModelProperty(value = "用户账号")
    private String storeMobile;
	@ApiModelProperty(value = "1、微信登录,2、账号登录")
	private Integer loginFlag;
	@ApiModelProperty(value = "微信openid")
    private String openid;
	@ApiModelProperty(value = "头像(微信登录方式传)")
    private String shopOwnerImg;
}
