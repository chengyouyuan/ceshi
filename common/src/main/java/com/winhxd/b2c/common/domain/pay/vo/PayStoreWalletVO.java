package com.winhxd.b2c.common.domain.pay.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayStoreWalletVO {
	private Long id;
    private Long storeId;
	@ApiModelProperty("openid")
	private String openid;
	@ApiModelProperty("昵称")
    private String nick;
	@ApiModelProperty("用户实名")
    private String name;
}
