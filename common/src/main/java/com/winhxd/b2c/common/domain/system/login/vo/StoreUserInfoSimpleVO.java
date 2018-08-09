package com.winhxd.b2c.common.domain.system.login.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author wufuyun
 * @date  2018年8月2日 下午4:38:19
 * @Description 
 * @version
 */
@ApiModel("惠小店登录返参")
@Data
public class StoreUserInfoSimpleVO{
	@ApiModelProperty(value = "TOKEN")
    private String token;

}