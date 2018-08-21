package com.winhxd.b2c.message.sms.enums;

/**
 * 短信发送状态枚举
 * */
public enum SmsSendStatusEnum {
	/**
	 * 发送成功与否枚举
	 */
	SUCCESS(1,"发送成功"),
	FAIL(0,"发送失败"),
	NOTSENT(2,"未发送");

	private int code;// 状态代码

	private String remark;// 描述

	SmsSendStatusEnum(int code, String remark) {
        this.code = code;
        this.remark = remark;
        
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
