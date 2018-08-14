package com.winhxd.b2c.pay.weixin.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * WithDrawChannelType
 *
 * @Author yindanqing
 * @Date 2018/8/14 9:55
 * @Description: 提现通道枚举
 */
public enum WithDrawChannelType {

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

    private WithDrawChannelType(int code, String text){

    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

}
