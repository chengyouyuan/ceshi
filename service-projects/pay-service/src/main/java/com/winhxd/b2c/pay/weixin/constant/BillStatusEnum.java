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
	PAYING((short)0, "支付中"),

	/**
	 * 支付完成
	 */
	PAID((short)1, "支付完成"),

	/**
	 * 支付失败
	 */
	FAIL((short)2, "支付失败");

	private Short code;
	private String text;

	private BillStatusEnum(Short code, String text) {
		this.code = code;
		this.text = text;
	}

	public Short getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

}
