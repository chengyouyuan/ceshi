package com.winhxd.b2c.pay.weixin.constant;

/**
 * 主动查询支付状态
 * @author mahongliang
 * @date  2018年8月21日 下午5:48:31
 * @Description 
 * @version
 */
public enum TradeStateEnum {
	/**
	 * 支付成功
	 */
	SUCCESS("SUCCESS", "支付成功"),

	/**
	 * 转入退款
	 */
	REFUND("REFUND", "转入退款"),

	/**
	 * 未支付
	 */
	NOTPAY("NOTPAY", "未支付"),
	
	/**
	 * 已关闭
	 */
	CLOSED("CLOSED", "已关闭"),

	/**
	 * 已撤销（刷卡支付）
	 */
	REVOKED("REVOKED", "已撤销"),
	
	/**
	 * 用户支付中
	 */
	USERPAYING("USERPAYING", "用户支付中"),

	/**
	 * 支付失败(其他原因，如银行返回失败)
	 */
	PAYERROR("PAYERROR", "支付失败");

	private String code;
	private String text;

	private TradeStateEnum(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public String getText() {
		return text;
	}
}
