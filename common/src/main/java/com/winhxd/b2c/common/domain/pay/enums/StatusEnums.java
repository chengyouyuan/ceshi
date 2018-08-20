package com.winhxd.b2c.common.domain.pay.enums;

public enum StatusEnums {
	/**
	 * 有效
	 */
	EFFECTIVE((short)1,"有效"),
	/**
	 * 无效
	 */
	INVALID((short)0,"无效");
	private Short code;
	private String desc;
	StatusEnums(Short code, String desc) {
        this.code = code;
        this.desc = desc;
    }
	public Short getCode() {
		return code;
	}
	public void setCode(Short code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
