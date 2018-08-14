package com.winhxd.b2c.common.domain.pay.enums;

import java.util.Map;
import java.util.TreeMap;

public enum PayOutTypeEnum {
	/**门店提现*/
    STORE_WITHDRAW((short) 1, "门店提现"),
    /**用户退款*/
    CUSTOMER_REFUND((short) 2, "用户退款");

    private short statusCode;
    private String statusDesc;

    PayOutTypeEnum(short statusCode, String statusDes) {
        this.statusCode = statusCode;
        this.statusDesc = statusDes;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
    
    public static PayOutTypeEnum getPayOutTypeEnumByCode(short code) {
        for (PayOutTypeEnum payOutTypeEnum : PayOutTypeEnum.values()) {
            if (payOutTypeEnum.getStatusCode() == code) {
                return payOutTypeEnum;
            }
        }
        return null;
    }

    public static String getPayOutTypeEnumDescByCode(Short code) {
        if (null != code) {
            for (PayOutTypeEnum payOutTypeEnum : PayOutTypeEnum.values()) {
                if (payOutTypeEnum.getStatusCode() == code) {
                    return payOutTypeEnum.getStatusDesc();
                }
            }
        }
        return null;
    }
    
    public static Map<Short, String> getDescMap() {
        Map<Short, String> map = new TreeMap<>();
        for (PayOutTypeEnum payOutTypeEnum : values()) {
            map.put(payOutTypeEnum.getStatusCode(), payOutTypeEnum.getStatusDesc());
        }
        return map;
    }
}
