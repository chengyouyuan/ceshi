package com.winhxd.b2c.common.domain.order.enums;

/**
 * @author pangjianhua
 * @date 2018/8/2 14:13
 */
public enum PayTypeEnum {
    /**
     * 微信扫码付款
     */
    WECHAT_SCAN_CODE_PAYMENT((short) 1, "微信扫码付款"),
    /**
     * 微信在线付款
     */
    WECHAT_ONLINE_PAYMENT((short) 2, "微信在线付款");
    private short typeCode;
    private String typeDesc;

    PayTypeEnum(short typeCode, String typeDesc) {
        this.typeCode = typeCode;
        this.typeDesc = typeDesc;
    }

    public short getTypeCode() {
        return typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public static PayTypeEnum getPayTypeEnumByTypeCode(int typeCode) {
        for (int i = 0; i < PayTypeEnum.values().length; i++) {
            if (PayTypeEnum.values()[i].getTypeCode() == typeCode) {
                return PayTypeEnum.values()[i];
            }
        }
        return null;
    }

    public static String getPayTypeEnumDescByTypeCode(Short typeCode) {
        if (null != typeCode) {
            for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
                if (typeCode == payTypeEnum.getTypeCode()) {
                    return payTypeEnum.getTypeDesc();
                }
            }
        }
        return null;
    }
}
