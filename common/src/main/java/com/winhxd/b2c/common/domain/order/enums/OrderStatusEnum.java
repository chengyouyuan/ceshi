package com.winhxd.b2c.common.domain.order.enums;

/**
 * 订单状态枚举
 *
 * @author pangjianhua
 * @date 2018/8/2 10:37
 */
public enum OrderStatusEnum {
    /**
     * 已提交 只有微信在线支付有这个状态
     */
    SUBMITTED((short)1, "订单已提交"),
    
     /**
     * 订单待商铺确认
     */
    RECEIVED((short)3, "订单待商铺确认"),
    /**
     * 待计价
     */
    WAIT_QUOTA_VALUATION((short)5, "待商铺确认价格"),
    /**
     * 商铺已确认价格，待用户确认
     */
    ALREADY_QUOTA_VALUATION((short)7, "商铺已确认价格，待用户确认"),
    /**
     * 待自提（已确认）
     */
    WAIT_SELF_LIFTING((short)9, "订单待提取，取货码：{0}"),
    /**
     * 待顾客确认
     */
    WAIT_CUSTOMER_CONFIRM((short)11, "待顾客确认"),
    /**
     * 已完成
     */
    FINISHED((short)22, "已完成"),
    /**
     * 已取消
     */
    CANCELED((short)99, "已取消"),
    /**
     * 已退款
     */
    REFUNDED((short)77, "已退款"),
    /**
     * 待退款
     */
    WAIT_REFUND((short)33, "待退款");


    private short statusCode;
    private String statusDes;

    OrderStatusEnum(short statusCode, String statusDes) {
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
