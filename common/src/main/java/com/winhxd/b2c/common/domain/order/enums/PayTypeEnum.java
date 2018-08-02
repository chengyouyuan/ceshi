package com.winhxd.b2c.common.domain.order.enums;

/**
 * @author pangjianhua
 * @date 2018/8/2 14:13
 */
public enum PayTypeEnum {
    /**
     * 微信扫码付款
     */
    WECHAT_SCAN_CODE_PAYMENT(1, "微信扫码付款"),
    /**
     * 微信在线付款
     */
    WECHAT_ONLINE_PAYMENT(2, "微信在线付款");
    private int typeCode;
    private String typeDesc;

    PayTypeEnum(int typeCode, String typeDesc) {
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