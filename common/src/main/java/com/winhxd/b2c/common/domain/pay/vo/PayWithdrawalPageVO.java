package com.winhxd.b2c.common.domain.pay.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("门店提现页面回显反参")
@Data
public class PayWithdrawalPageVO {
	@ApiModelProperty("可提现金额")
	private BigDecimal presented_money;	
	@ApiModelProperty("本次提现-单笔最高额度")
	private BigDecimal total_moeny;
	@ApiModelProperty("提现账户-微信昵称或者银行卡名称(卡号后四位)")
	private String userAcountName;
}
