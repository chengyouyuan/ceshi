package com.winhxd.b2c.common.domain.order.enums;

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
    private String statusDes;

    PayStatusEnum(short statusCode, String statusDes) {
        this.statusCode = statusCode;
        this.statusDes = statusDes;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public String getStatusDes() {
        return statusDes;
    }
}
