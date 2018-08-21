package com.winhxd.b2c.message.sms.enums;

/**
 * 短信类型枚举
 */
public enum SmsTypeEnum {
	/**
	 * 验证码短信
	 */
	VERIFICATION(1, "验证码短信"),
	VOICE(2, "语音短信"),
	MARKETING(3, "通知、营销短信"),
	INTERNATIONAL(4, "国际文字短信"),
	INTERNATIONAL_VOICE(5, "国际语音短信");
	private int type;// 类型代码

	private String remark;// 描述

	SmsTypeEnum(int type, String remark) {
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
