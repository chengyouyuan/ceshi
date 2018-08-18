package com.winhxd.b2c.common.domain.pay.condition;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateStoreBankRollCondition {

	
	@ApiModelProperty("类型：1.支付完成,2.结算审核,3.提现申请,4.提现成功,5提现审核不通过,6提现失败")
    private Integer type;
	
	@ApiModelProperty("费用类型：1.货款审核,2.促销费用")
	private Short moneyType;
	
	@ApiModelProperty("门店Id")
	private Long storeId;
	
	@ApiModelProperty("订单号")
    private String orderNo;
    
	@ApiModelProperty("提现单号")
    private String withdrawalsNo;
	
	@ApiModelProperty("金额")
    private BigDecimal money;
}
