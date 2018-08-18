package com.winhxd.b2c.common.domain.store.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author wufuyun
 * @date  2018年8月6日 下午6:00:43
 * @Description 
 * @version
 */
public enum StoreStatusEnum {

    /**
     * 未开店
     */
    UN_OPEN((short) 0, "未开店"),
    /**
     * 有效
     */
    VALID((short) 1, "有效"),
    /**
     * 无效
     */
    INVALID((short) 2, "无效");

    private short statusCode;
    private String statusDesc;

    StoreStatusEnum(short statusCode, String statusDes) {
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
            for (StoreStatusEnum storeStatusEnum : StoreStatusEnum.values()) {
                if (code == storeStatusEnum.getStatusCode()) {
                    return storeStatusEnum.getStatusDesc();
                }
            }
        }
        return null;
    }

    public static Map<Short, String> getDescMap() {
        Map<Short, String> map = new TreeMap<>();
        for (StoreStatusEnum storeStatusEnum : values()) {
            map.put(storeStatusEnum.getStatusCode(), storeStatusEnum.getStatusDesc());
        }
        return map;
    }

}
