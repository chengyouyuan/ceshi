package com.winhxd.b2c.message.sms.enums;

/**
 * 短信来源
 */
public enum SmsSourcesEnum {
	/**
	 * 短信来源
	 */
	REGISTER("110", "注册"),
	RETRIEVE("113", "找回密码"),
	INVITATION("business", "业代邀请");
	private String type;

	private String remark;

	SmsSourcesEnum(String type, String remark) {
		this.type = type;
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public String getRemark() {
		return remark;
	}
}
