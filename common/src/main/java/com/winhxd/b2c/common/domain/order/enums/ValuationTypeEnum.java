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
    ONLINE_VALUATION((short) 1, "线上计价"),
    /**
     * 线下计价
     */
    OFFLINE_VALUATION((short) 2, "线下计价");

    private short typeCode;
    private String typeDesc;

    ValuationTypeEnum(short typeCode, String typeDesc) {
        this.typeCode = typeCode;
        this.typeDesc = typeDesc;
    }

    public short getTypeCode() {
        return typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public static String getDescByCode(Short code) {
        if (null != code) {
            for (ValuationTypeEnum typeEnum : ValuationTypeEnum.values()) {
                if (typeEnum.getTypeCode() == code) {
                    return typeEnum.getTypeDesc();
                }
            }
        }
        return null;
    }
}
