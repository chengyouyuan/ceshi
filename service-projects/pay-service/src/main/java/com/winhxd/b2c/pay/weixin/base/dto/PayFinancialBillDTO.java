package com.winhxd.b2c.pay.weixin.base.dto;

/**
 * 下载对账单入参
 * 
 * @author yuluyuan
 *
 *         2018年8月19日
 */
public class PayFinancialBillDTO extends RequestBase {

	/**
	 * 对账单日期
	 */
	private String billDate;

	/**
	 * 资金账户类型
	 */
	private String accountType;

	/**
	 * 压缩账单 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
	 */
	private String tarType;

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getTarType() {
		return tarType;
	}

	public void setTarType(String tarType) {
		this.tarType = tarType;
	}

	@Override
	public String toString() {
		return "PayFinancialBillDTO{" +
				"billDate='" + billDate + '\'' +
				", accountType='" + accountType + '\'' +
				", tarType='" + tarType + '\'' +
				'}';
	}
}
