package com.winhxd.b2c.pay.weixin.constant;

/**
 * 账单类型枚举
 * 
 * @author yuluyuan
 *
 *         2018年8月16日
 */
public enum BillType {

	/**
	 * 对账单
	 */
	STATEMENT(1, "对账单"),

	/**
	 * 对账单统计
	 */
	STATEMENT_COUNT(2, "对账单统计"),

	/**
	 * 资金账单
	 */
	FINANCIAL_BILL(3, "资金账单"),

	/**
	 * 资金账单统计
	 */
	FINANCIAL_BILL_COUNT(4, "资金账单统计");

	private int code;
	private String text;

	private BillType(int code, String text) {
		this.code = code;
		this.text = text;
	}

	public int getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

}
