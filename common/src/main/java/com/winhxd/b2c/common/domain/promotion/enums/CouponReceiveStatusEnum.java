package com.winhxd.b2c.common.domain.promotion.enums;

public enum CouponReceiveStatusEnum {

    ALREADY_RECEIVED("0", "已领取"),
    CAN_RECEIVED("1", "可领取"),
    HAVE_FINISHED("2", "已领完");


    private String code;
    private String desc;

    CouponReceiveStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }
}
