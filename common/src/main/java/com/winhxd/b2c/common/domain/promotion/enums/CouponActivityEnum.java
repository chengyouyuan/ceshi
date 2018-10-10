package com.winhxd.b2c.common.domain.promotion.enums;

/**
 *
 * @author shijinxing
 * @date 2018/8/7
 */
public enum CouponActivityEnum {
    /**
     * 优惠券活动类型
     */
    PULL_COUPON((short) 1,"领券"),
    PUSH_COUPON((short) 2,"推券"),

    /**
     * 优惠券活动是否有效  0无效 1有效
     */
    ACTIVITY_INVALID((short) 0, "无效"),
    ACTIVITY_EFFICTIVE((short) 1, "有效"),


    /**
     * 活动状态 1开启 2停止
     */
    ACTIVITY_OPEN((short) 1,"开启"),
    ACTIVITY_STOP((short) 2,"停止"),

    /**
     * 优惠券类型 1新用户注册 2老用户活动
     */
    NEW_USER((short) 1, "新用户"),
    OLD_USER((short) 2, "老用户"),

    /**
     * 优惠券数量的限制 1优惠券总数2每个门店优惠券数
     */
    COUPON_SUM((short) 1, "优惠券总数"),
    STORE_NUM((short) 2, "每个门店优惠券数"),

    /**
     * 用户领券限制 1不限制 2每个门店可领取数量
     */
    UNLIMITED((short) 1, "不限制"),
    STORE_LIMITED((short) 2, "每个门店可领取数量"),

    /**
     * 优惠券状态 0 无效 1-已使用，2-未使用， 3-已过期,4-退回
     */
    ALREADY_USE((short) 1, "已使用"),
    NOT_USE((short) 2, "未使用"),
    INVALYD((short) 0, "无效"),
    EXPIRED((short) 3, "已过期"),
    UNTREAD((short) 4, "退回"),
    FAST_EXPIRED((short)5,"快过期"),

    /**
     * 优惠券来源 1-系统发放
     */
    SYSTEM((short) 1, "系统发放"),

    /**
     * 发放对象 1-普通用户
     */
    ORDINARY_USER((short) 1, "普通用户");


    private short code;
    private String desc;

    CouponActivityEnum(short code,String desc){
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
