package com.winhxd.b2c.common.domain.order.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author pangjianhua
 * @date 2018/8/2 14:11
 */
public enum PayStatusEnum {
    /**
     * 未支付
     */
    UNPAID((short) 0, "未支付"),
    /**
     * 已支付支付
     */
    PAID((short) 1, "已支付");

    private short statusCode;
    private String statusDesc;

    PayStatusEnum(short statusCode, String statusDes) {
        this.statusCode = statusCode;
        this.statusDesc = statusDes;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public static String getDesc(Short code) {
        if (null != code) {
            for (PayStatusEnum payStatusEnum : PayStatusEnum.values()) {
                if (code == payStatusEnum.getStatusCode()) {
                    return payStatusEnum.getStatusDesc();
                }
            }
        }
        return null;
    }
    
    public static Map<Short, String> getDescMap() {
        Map<Short, String> map = new TreeMap<>();
        for (PayStatusEnum payStatusEnum : values()) {
            map.put(payStatusEnum.getStatusCode(), payStatusEnum.getStatusDesc());
        }
        return map;
    }
}
