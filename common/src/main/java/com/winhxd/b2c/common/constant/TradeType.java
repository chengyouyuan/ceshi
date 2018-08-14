package com.winhxd.b2c.common.constant;

/**
 * TradeType
 *
 * @Author yindanqing
 * @Date 2018/8/14 16:04
 * @Description: 交易类型枚举
 */
public enum TradeType {

    JSAPI("JSAPI","公众号支付"),
    NATIVE("NATIVE","扫码支付"),
    APP("APP","APP支付");

    private TradeType(String code, String name){
        this.code = code;
        this.name = name;
    }

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getText() {
        return name;
    }

}
