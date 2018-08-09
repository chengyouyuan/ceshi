package com.winhxd.b2c.common.domain.promotion.enums;

/**
 * @Author wl
 * @Date 2018/8/9 11:09
 * @Description
 **/
public enum CouponApplyEnum {
    COMMON_COUPON((short) 1,"通用券"),
    BRAND_COUPON((short) 2,"品牌券"),
    CAT_COUPON((short) 3,"品类券"),
    PRODUCT_COUPON((short) 4,"商品券");

    private short code;
    private String desc;

    CouponApplyEnum(short code,String desc){
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
