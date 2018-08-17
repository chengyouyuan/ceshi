package com.winhxd.b2c.common.domain.pay.enums;

public enum StoreBankRollOpearateEnums {
	/**
	 * 订单完成操作
	 */
	ORDER_FINISH(1,"订单完成"),
	/**
	 * 结算审核操作
	 */
	SETTLEMENT_AUDIT(2,"结算审核"),
	/**
	 * 提现申请操作
	 */
	withdrawals_apply(3,"提现申请"),
	/**
	 * 提现审核操作
	 */
	withdrawals_audit(4,"提现审核");
	
	private Integer code;
	private String desc;
	StoreBankRollOpearateEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
