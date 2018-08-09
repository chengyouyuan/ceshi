package com.winhxd.b2c.common.domain.system.login.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author wufuyun
 * @date  2018年8月9日 下午12:20:07
 * @Description 
 * @version
 */
@ApiModel("C端发送验证码请求参数")
@Data
public class CustomerSendVerificationCodeCondition {
	@ApiModelProperty(value = "用户账号")
    private String customerMobile;
}
