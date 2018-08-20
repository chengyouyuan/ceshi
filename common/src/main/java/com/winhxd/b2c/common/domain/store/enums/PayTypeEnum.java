package com.winhxd.b2c.common.domain.store.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * 支付方式，门店功能专用
 *
 * @author liutong
 * @date 2018/8/20 14:13
 */
public enum PayTypeEnum {
    /**
     * 微信在线付款
     */
    PAYMENT_WECHAT_ONLINE((short) 1, "微信在线付款"),
    /**
     * 微信扫码付款
     */
    PAYMENT_WECHAT_SCAN_CODE((short) 2, "微信扫码付款");
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


    public static Map<Short, String> getDescMap() {
        Map<Short, String> map = new TreeMap<>();
        for (PayTypeEnum payTypeEnum : values()) {
            map.put(payTypeEnum.getTypeCode(), payTypeEnum.getTypeDesc());
        }
        return map;
    }
}
