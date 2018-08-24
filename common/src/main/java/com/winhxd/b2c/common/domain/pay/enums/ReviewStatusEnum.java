package com.winhxd.b2c.common.domain.pay.enums;

/**
 * 审核状态 
 * */
public enum ReviewStatusEnum {
    
	/**
	 * 超阈值订单审核状态，0：待审核
	 */
    TO_AUDIT((short)0,"待审核"),
	/**
	 * 超阈值订单审核状态，1：审核通过
	 */
    PASS((short)1,"通过"),
	/**
	 * 超阈值订单审核状态，2：审核未通过
	 */
    NOT_PASS((short)2,"不通过");
    
    private short status;// 状态
    
    private String remark;// 说明
    
    private ReviewStatusEnum(short status,String remark) {
        this.status = status;
        this.remark = remark;
    }

    public short getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }
}
