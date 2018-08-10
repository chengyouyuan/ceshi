package com.winhxd.b2c.common.domain.system.login.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author wufuyun
 * @date  2018年8月2日 下午4:38:38
 * @Description 
 * @version
 */
@ApiModel("惠小店用户请求参数")
@Data
public class StoreUserInfoCondition{
	@ApiModelProperty(value = "用户账号")
    private String storeMobile;
	@ApiModelProperty(value = "密码")
    private String storePassword;
	@ApiModelProperty(value = "头像")
    private String shopOwnerImg;
	@ApiModelProperty(value = "微信openid")
    private String openid;
	@ApiModelProperty(value = "来源")
    private String source;
	@ApiModelProperty(value = "短信验证码")
	private String verificationCode;
	@ApiModelProperty(value = "1、微信登录,2、账号登录")
	private Integer loginFlag;
	@ApiModelProperty(value = "1、验证码登录，2、密码登录,3、快捷登录")
	private Integer loginPasswordFlag;
}