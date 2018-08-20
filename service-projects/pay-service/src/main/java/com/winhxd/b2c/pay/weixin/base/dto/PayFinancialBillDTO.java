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

	public enum SourceType {
		/**
		 * 对账单
		 */
		BASIC("BASIC", "基本账户"),

		/**
		 * 对账单统计
		 */
		OPERATION("OPERATION", "运营账户"),

		/**
		 * 资金账单
		 */
		FEES("FEES", "手续费账户");

		private String text;
		private String desc;

		private SourceType(String text, String desc) {
			this.text = text;
			this.desc = desc;
		}

		public String getText() {
			return text;
		}

		public String getDesc() {
			return desc;
		}
	}
}
