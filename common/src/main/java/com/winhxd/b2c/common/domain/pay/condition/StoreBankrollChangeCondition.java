package com.winhxd.b2c.common.domain.pay.condition;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StoreBankrollChangeCondition {
	@ApiModelProperty("门店Id")
	private Long storeId;


	@ApiModelProperty("门店总收入")
    private BigDecimal totalMoeny;
	
	@ApiModelProperty("门店可提现金额")
	private BigDecimal presentedMoney;

	@ApiModelProperty("冻结金额")
    private BigDecimal presentedFrozenMoney;

	@ApiModelProperty("待结算金额")
    private BigDecimal settlementSettledMoney;
	
	@ApiModelProperty("门店已提现金额")
    private BigDecimal alreadyPresentedMoney;
    
	@ApiModelProperty("描述")
    private String remarks;
	
	@ApiModelProperty("类型：1.支付完成,2.结算审核,3.提现申请,4.提现成功,5提现审核不通过,6提现失败")
    private Integer type;
	
	@ApiModelProperty("费用类型：1.货款审核,2.促销费用")
	private Short moneyType;
	
    @ApiModelProperty("订单号")
    private String orderNo;
    
    @ApiModelProperty("提现单号")
    private String withdrawalsNo;
    
    @ApiModelProperty("变化金额")
    private BigDecimal money;
    
}
