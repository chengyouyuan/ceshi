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
public class StoreUserInfoVO{
	@ApiModelProperty(value = "用户id")
    private Long businessId;
	@ApiModelProperty(value = "门店名称")
    private String storeName;
	@ApiModelProperty(value = "门店编码")
    private Long storeId;
	@ApiModelProperty(value = "账号")
    private String storeMobile;
	@ApiModelProperty(value = "地址")
    private String storeAddress;
	@ApiModelProperty(value = "店主名称")
    private String shopkeeper;
	@ApiModelProperty(value = "店主头像")
    private String shopOwnerUrl;

}