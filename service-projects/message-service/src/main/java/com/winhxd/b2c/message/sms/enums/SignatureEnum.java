package com.winhxd.b2c.message.sms.enums;

/**
 * 短信签名
 */
public enum SignatureEnum {
	/**
	 * 惠下单短信签名
	 */
	RETAIL(1, "【惠下单】"),
	INTERNATIONAL(3, "【Winchannel】"),
	RESTAURANT(2, "【惠餐饮】");
	private int type;// 类型代码
	private String remark;// 描述

	SignatureEnum(int type, String remark) {
		this.type = type;
		this.remark = remark;
	}

	public int getType() {
		return type;
	}

	public String getRemark() {
		return remark;
	}
}
