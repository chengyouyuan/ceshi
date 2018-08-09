package com.winhxd.b2c.common.domain.system.login.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author wufuyun
 * @date  2018年8月2日 下午3:58:36
 * @Description 
 * @version
 */
@ApiModel("小程序用户请求参数")
@Data
public class CustomerUserInfoCondition{
	@ApiModelProperty(value = "账号")
    private String customerMobile;
	@ApiModelProperty(value = "微信code")
    private String code;
	@ApiModelProperty(value = "纬度")
    private Double lat;
	@ApiModelProperty(value = "经度")
    private Double lon;
	@ApiModelProperty(value = "昵称")
    private String nickName;
	@ApiModelProperty(value = "头像")
    private String headurl;
	@ApiModelProperty(value = "短信验证码")
	private String verificationCode;
}