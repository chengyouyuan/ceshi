package com.winhxd.b2c.common.domain.order.enums;

/**
 * @author pangjianhua
 * @date 2018/8/2 14:11
 */
public enum PayStatusEnum {
    /**
     * 未支付
     */
    UNPAID(0, "未支付"),
    /**
     * 已支付支付
     */
    PAID(0, "已支付");

    private int statusCode;
    private String statusDes;

    PayStatusEnum(int statusCode, String statusDes) {
        this.statusCode = statusCode;
        this.statusDes = statusDes;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusDes() {
        return statusDes;
    }
}
