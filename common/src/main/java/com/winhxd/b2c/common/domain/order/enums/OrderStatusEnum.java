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
    SUBMITTED(1, "已提交"),
    /**
     * 已接单
     */
    RECEIVED(3, "已接单"),
    /**
     * 待计价
     */
    WAIT_VALUATION(5, "待计价"),
    /**
     * 已计价
     */
    ALREADY_VALUATION(7, "已计价"),
    /**
     * 待自提（已确认）
     */
    WAIT_SELF_LIFTING(9, "待自提"),
    /**
     * 待顾客确认
     */
    WAIT_CUSTOMER_CONFIRM(11, "待顾客确认"),
    /**
     * 已完成
     */
    FINISHED(13, "已完成"),
    /**
     * 已取消
     */
    CANCELED(99, "已取消"),
    /**
     * 已退款
     */
    REFUNDED(77, "已取消"),
    /**
     * 待退款
     */
    WAIT_REFUND(33, "待退款");


    private int statusCode;
    private String statusDes;

    OrderStatusEnum(int statusCode, String statusDes) {
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
