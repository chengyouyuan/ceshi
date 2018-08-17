package com.winhxd.b2c.pay.weixin.constant;

/**
 * TransfersChannelType
 *
 * @Author yindanqing
 * @Date 2018/8/14 9:55
 * @Description: 转账通道枚举
 */
public enum TransfersChannelType {

    /**
     * 微信余额
     */
    WXBALANCE(1,"微信余额"),

    /**
     * 微信银行卡
     */
    WXBBANK(2,"微信银行卡");

    private int code;
    private String text;

    private TransfersChannelType(int code, String text){

    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

}
