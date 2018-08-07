package com.winhxd.b2c.common.domain.backstage.store.enums;

/**
 * @title: retail2c-backend
 * @description:
 * @author: lvsen
 * @date: 2018/8/3 15:58
 * @version: 1.0
 */
public enum BackStageStorPaymentWayeEnum {
    /**
     * 支付方式
     */
    PAYMENT_WAY_WX_ONLINE("1", "微信在线付款"),

    PAYMENT_WAY_WX_SCAN("2", "微信扫码付款");


    private String status;
    private String desc;

    BackStageStorPaymentWayeEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String getStatusCode() {
        return status;
    }

    public String getStatusDes() {
        return desc;
    }

    public static BackStageStorPaymentWayeEnum codeOf(String code){
        for(BackStageStorPaymentWayeEnum paymentWayeEnum : values()){
            if(paymentWayeEnum.getStatusCode().equals(code)){
                return paymentWayeEnum;
            }
        }
        return null;
    }
}
