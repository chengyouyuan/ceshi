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
	@ApiModelProperty("费率")
    private BigDecimal rate;
	
	@ApiModelProperty("银行卡号")
    private String cardNumber;
	@ApiModelProperty("身份证号")
    private String personId;
	@ApiModelProperty("开户人姓名")
    private String bankUserName;
	@ApiModelProperty("银行名称")
    private String bankName;
	@ApiModelProperty("银行swiftCode")
	private String swiftCode;
	@ApiModelProperty("银行支行或者分行名称")
    private String bandBranchName;
	@ApiModelProperty("手机号")
	private String mobile;
	@ApiModelProperty("微信openid")
    private String openid;
	@ApiModelProperty("微信昵称")
    private String nick;
}
