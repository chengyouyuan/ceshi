package com.winhxd.b2c.common.domain.store.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author lvsen
 * @date  2018年8月6日 下午6:00:43
 * @Description 
 * @version
 */
public enum StorePayTypeEnum {

    /**
     * 1、微信在线付款
     */
    WECHAT_ONLINE_PAY("1", "微信在线付款"),
    /**
     * 2、微信扫码付款
     */
    WECHAT_SCAN_PAY("2", "微信扫码付款");

    private String statusCode;
    private String statusDesc;

    StorePayTypeEnum(String statusCode, String statusDes) {
        this.statusCode = statusCode;
        this.statusDesc = statusDes;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public static String getDesc(String code) {
        if (null != code) {
            for (StorePayTypeEnum storePayTypeEnum : StorePayTypeEnum.values()) {
                if (code.equals(storePayTypeEnum.getStatusCode())) {
                    return storePayTypeEnum.getStatusDesc();
                }
            }
        }
        return null;
    }

    public static Map<String, String> getDescMap() {
        Map<String, String> map = new TreeMap<>();
        for (StorePayTypeEnum storePayTypeEnum : values()) {
            map.put(storePayTypeEnum.getStatusCode(), storePayTypeEnum.getStatusDesc());
        }
        return map;
    }

}
