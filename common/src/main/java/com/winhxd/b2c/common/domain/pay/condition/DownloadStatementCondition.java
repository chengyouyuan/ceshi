package com.winhxd.b2c.common.domain.pay.condition;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import com.winhxd.b2c.common.domain.common.ApiCondition;

/**
 * 下载对账单、资金账单 参数
 * @author yuluyuan
 *
 * 2018年8月20日
 */
public class DownloadStatementCondition extends ApiCondition implements Serializable {

	private static final long serialVersionUID = 3641877835985641570L;

	@ApiModelProperty("对账单日期")
    private Date billDate;

    @ApiModelProperty("账单类型ALL，返回当日所有订单信息，默认值; SUCCESS，返回当日成功支付的订单; REFUND，返回当日退款订单; RECHARGE_REFUND，返回当日充值退款订单")
    private String StatementType;

    @ApiModelProperty("账单的资金来源账户：Basic 基本账户; Operation 运营账户; Fees 手续费账户")
    private String accountType;

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public String getStatementType() {
		return StatementType;
	}

	public void setStatementType(String statementType) {
		StatementType = statementType;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
