package com.winhxd.b2c.common.domain.order.enums;

/**
 * 计价类型
 *
 * @author pangjianhua
 * @date 2018/8/2 14:19
 */
public enum ValuationTypeEnum {
    /**
     * 线上计价
     */
    ONLINE_VALUATION(1, "线上计价"),
    /**
     * 线下计价
     */
    OFFLINE_VALUATION(2, "线下计价");

    private int typeCode;
    private String typeDesc;

    ValuationTypeEnum(int typeCode, String typeDesc) {
        this.typeCode = typeCode;
        this.typeDesc = typeDesc;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }
}
