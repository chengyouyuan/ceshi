package com.winhxd.b2c.common.domain.pay.condition;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StoreBankrollChangeCondition {
	@ApiModelProperty("门店Id")
	private Long storeId;


	@ApiModelProperty("门店可提现金额")
    private BigDecimal presentedMoney;

	@ApiModelProperty("冻结金额")
    private BigDecimal presentedFrozenMoney;

	@ApiModelProperty("待结算金额")
    private BigDecimal settlementSettledMoney;
    
	@ApiModelProperty("描述")
    private String remarks;
	
	@ApiModelProperty("类型：1.支付完成,2.结算审核,3.提现申请,4.提现审核")
    private Integer type;
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("提现单号")
    private String withdrawalsNo;
    
}
