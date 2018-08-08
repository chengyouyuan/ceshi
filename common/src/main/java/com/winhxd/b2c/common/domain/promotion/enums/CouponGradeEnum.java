package com.winhxd.b2c.common.domain.promotion.enums;

/**
 * @Author wl
 * @Date 2018/8/8 17:10
 * @Description
 **/
public enum CouponGradeEnum {

    /**
     * 坎级规则类型   1-满减/2-满赠/3-按件减阶梯/4-按件减翻倍/5-按件增阶梯/6-按件增翻倍)
     */
     UP_TO_REDUCE((short)1,"满减"),
     UP_TO_DISCOUNT((short)2,"满减"),
     REDUCE_BY_STAGE((short)3,"按件减阶梯"),
     TIMES_REDUCE_BY_COUNT((short)4,"按件减翻倍"),
     REDUCE_BY_UP_STAGE((short)5,"按件增阶梯"),
     TIMES_UP_STAGE((short)6,"按件增翻倍"),

    /***
     * 满减优惠类型
     */
    UP_TO_REDUCE_CASH((short)1,"金额"),
    UP_TO_REDUCE_DISC((short)2,"折扣");

    private short code;
    private String desc;

    CouponGradeEnum(short code,String desc){
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
