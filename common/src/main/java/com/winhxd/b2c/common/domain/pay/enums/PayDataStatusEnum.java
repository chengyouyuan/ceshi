package com.winhxd.b2c.common.domain.pay.enums;

import java.util.Map;
import java.util.TreeMap;

public enum PayDataStatusEnum {

	/**预入账*/
    PRE_INCOUNT((short) 1, "预入账"),
    /**实入账*/
    REAL_INCOUNT((short) 2, "实入账");

    private short statusCode;
    private String statusDesc;

    PayDataStatusEnum(short statusCode, String statusDes) {
        this.statusCode = statusCode;
        this.statusDesc = statusDes;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
    
    public static PayDataStatusEnum getPayDataStatusEnumByCode(short code) {
        for (PayDataStatusEnum payDataStatusEnum : PayDataStatusEnum.values()) {
            if (payDataStatusEnum.getStatusCode() == code) {
                return payDataStatusEnum;
            }
        }
        return null;
    }

    public static String getPayDataStatusEnumDescByCode(Short code) {
        if (null != code) {
            for (PayDataStatusEnum payDataStatusEnum : PayDataStatusEnum.values()) {
                if (payDataStatusEnum.getStatusCode() == code) {
                    return payDataStatusEnum.getStatusDesc();
                }
            }
        }
        return null;
    }
    
    public static Map<Short, String> getDescMap() {
        Map<Short, String> map = new TreeMap<>();
        for (PayDataStatusEnum payDataStatusEnum : values()) {
            map.put(payDataStatusEnum.getStatusCode(), payDataStatusEnum.getStatusDesc());
        }
        return map;
    }

}
