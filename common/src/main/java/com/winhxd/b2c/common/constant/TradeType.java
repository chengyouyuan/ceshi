package com.winhxd.b2c.common.constant;

/**
 * TradeType
 *
 * @Author yindanqing
 * @Date 2018/8/14 16:04
 * @Description: 交易类型枚举
 */
public enum TradeType {
    //wechat
    WECHAT_H5("JSAPI","微信公众号支付"),
    WECHAT_CSB("NATIVE","微信扫码支付"),
    WECHAT_APP("APP","微信支付");

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
