package com.winhxd.b2c.common.domain.pay.enums;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/17 15 06
 * @Description
 */
public enum TradeTypeEnums {
    JSAPI("JSAPI", "公众号支付"),

    NATIVE("NATIVE", "扫码支付"),

    APP("APP", "APP支付");

    private String code;
    private String desc;

    TradeTypeEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
