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
    private String shopOwnerUrl;
	@ApiModelProperty(value = "纬度")
    private Double lat;
	@ApiModelProperty(value = "经度")
    private Double lon;
	@ApiModelProperty(value = "微信openid")
    private String openid;
	@ApiModelProperty(value = "来源")
    private String source;
	@ApiModelProperty(value = "短信验证码")
	private String verificationCode;
}