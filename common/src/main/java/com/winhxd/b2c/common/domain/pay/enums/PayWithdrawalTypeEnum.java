package com.winhxd.b2c.common.domain.pay.enums;

import java.util.Map;
import java.util.TreeMap;

public enum PayWithdrawalTypeEnum {
    WECHART_WITHDRAW((short) 1, "微信提现"),
    /**用户退款*/
    BANKCARD_WITHDRAW((short) 2, "银行卡提现");

    private short statusCode;
    private String statusDesc;

    PayWithdrawalTypeEnum(short statusCode, String statusDes) {
        this.statusCode = statusCode;
        this.statusDesc = statusDes;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
    
    public static PayWithdrawalTypeEnum getPayOutTypeEnumByCode(short code) {
        for (PayWithdrawalTypeEnum payOutTypeEnum : PayWithdrawalTypeEnum.values()) {
            if (payOutTypeEnum.getStatusCode() == code) {
                return payOutTypeEnum;
            }
        }
        return null;
    }

    public static String getPayOutTypeEnumDescByCode(Short code) {
        if (null != code) {
            for (PayWithdrawalTypeEnum payOutTypeEnum : PayWithdrawalTypeEnum.values()) {
                if (payOutTypeEnum.getStatusCode() == code) {
                    return payOutTypeEnum.getStatusDesc();
                }
            }
        }
        return null;
    }
    
    public static Map<Short, String> getDescMap() {
        Map<Short, String> map = new TreeMap<>();
        for (PayWithdrawalTypeEnum payOutTypeEnum : values()) {
            map.put(payOutTypeEnum.getStatusCode(), payOutTypeEnum.getStatusDesc());
        }
        return map;
    }
}
