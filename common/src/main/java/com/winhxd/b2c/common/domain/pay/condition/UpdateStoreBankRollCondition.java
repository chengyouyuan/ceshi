package com.winhxd.b2c.common.domain.pay.condition;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateStoreBankRollCondition {

	
	@ApiModelProperty("类型：1.支付完成,2.结算审核,3.提现申请,4.提现审核")
    private Integer type;
	
	@ApiModelProperty("门店Id")
	private Long storeId;
	
	@ApiModelProperty("订单号")
    private String orderNo;
    
	@ApiModelProperty("提现单号")
    private String withdrawalsNo;
	
	@ApiModelProperty("金额")
    private BigDecimal money;
}
