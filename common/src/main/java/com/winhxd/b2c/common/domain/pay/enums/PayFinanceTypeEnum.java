package com.winhxd.b2c.common.domain.pay.enums;

import java.util.Map;
import java.util.TreeMap;

public enum PayFinanceTypeEnum {

    /**
     * 1出账
     */
    OUTMONEY((short) 1, "出账"),
    /**
     * 入账
     */
    INMONEY((short) 2, "入账");

    private short statusCode;
    private String statusDesc;

    PayFinanceTypeEnum(short statusCode, String statusDes) {
        this.statusCode = statusCode;
        this.statusDesc = statusDes;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
    
    public static PayFinanceTypeEnum getPayFinanceTypeEnumByCode(short code) {
        for (PayFinanceTypeEnum payFinanceTypeEnum : PayFinanceTypeEnum.values()) {
            if (payFinanceTypeEnum.getStatusCode() == code) {
                return payFinanceTypeEnum;
            }
        }
        return null;
    }

    public static String getPayFinanceTypeDescByCode(Short code) {
        if (null != code) {
            for (PayFinanceTypeEnum payFinanceTypeEnum : PayFinanceTypeEnum.values()) {
                if (payFinanceTypeEnum.getStatusCode() == code) {
                    return payFinanceTypeEnum.getStatusDesc();
                }
            }
        }
        return null;
    }
    
    public static Map<Short, String> getDescMap() {
        Map<Short, String> map = new TreeMap<>();
        for (PayFinanceTypeEnum payFinanceTypeEnum : values()) {
            map.put(payFinanceTypeEnum.getStatusCode(), payFinanceTypeEnum.getStatusDesc());
        }
        return map;
    }

}
