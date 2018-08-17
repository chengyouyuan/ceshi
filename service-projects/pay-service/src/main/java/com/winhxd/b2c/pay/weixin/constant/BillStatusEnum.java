package com.winhxd.b2c.pay.weixin.constant;

/**
 * 对账单支付状态
 * @author mahongliang
 * @date  2018年8月17日 下午5:21:05
 * @Description 
 * @version
 */
public enum BillStatusEnum {

	/**
	 * 支付中
	 */
	PAYING(0, "支付中"),

	/**
	 * 支付完成
	 */
	PAID(1, "支付完成"),

	/**
	 * 支付失败
	 */
	FAIL(2, "支付失败");

	private int code;
	private String text;

	private BillStatusEnum(int code, String text) {
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
