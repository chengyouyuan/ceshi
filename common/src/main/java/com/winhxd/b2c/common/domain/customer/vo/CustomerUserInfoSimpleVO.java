package com.winhxd.b2c.common.domain.customer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wufuyun
 * @date  2018年8月2日 下午4:38:25
 * @Description 
 * @version
 */
@ApiModel("小程序登录返参")
@Data
public class CustomerUserInfoSimpleVO {
	@ApiModelProperty(value = "账号")
    private String customerMobile;
	@ApiModelProperty(value = "TOKEN")
    private String token;
    @ApiModelProperty(value = "C端用户微信openid")
    private String openid;
}