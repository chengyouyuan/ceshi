package com.winhxd.b2c.common.domain.promotion.enums;

public enum CouponAvailableStatusEnum {

    AVAILABLE((short) 1, "可用"),
    UN_AVAILABLE((short) 0, "不可用");

    private short code;
    private String desc;

    CouponAvailableStatusEnum(short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public short getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }
}
