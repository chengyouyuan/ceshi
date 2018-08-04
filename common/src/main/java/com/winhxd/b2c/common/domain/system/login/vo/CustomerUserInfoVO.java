package com.winhxd.b2c.common.domain.system.login.vo;

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
public class CustomerUserInfoVO {
	@ApiModelProperty(value = "用户主键")
    private Long customerId;
	@ApiModelProperty(value = "账号")
    private String customerMobile;

}