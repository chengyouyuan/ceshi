package com.winhxd.b2c.common.domain.pay.enums;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/18 12 17
 * @Description
 */
public enum StoreTransactionStatusEnum {
    ORDER_ENTRY((short) 1, "订单入账"),

    TRANSFERS((short) 2, "提现");

    private short statusCode;
    private String statusDesc;

    StoreTransactionStatusEnum(short statusCode, String statusDesc) {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(short statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
