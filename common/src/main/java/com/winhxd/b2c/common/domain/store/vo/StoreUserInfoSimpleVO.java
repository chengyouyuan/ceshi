package com.winhxd.b2c.common.domain.store.vo;

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
public class StoreUserInfoSimpleVO {
	@ApiModelProperty(value = "TOKEN")
    private String token;
	@ApiModelProperty(value = "云信账号id")
	private String neteaseAccid;
	@ApiModelProperty(value = "云信token")
	private String neteaseToken;
	@ApiModelProperty(value = "惠下单门店用户id")
    private Long customerId;
	@ApiModelProperty(value = "用户账号")
	private String storeMobile;

}