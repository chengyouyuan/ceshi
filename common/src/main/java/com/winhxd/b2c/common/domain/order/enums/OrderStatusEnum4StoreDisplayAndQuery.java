package com.winhxd.b2c.common.domain.order.enums;

/**
 * 门店版使用的订单状态枚举，用于门店端查询和展示
 *
 * @author wangbin
 * @date 2018/8/7 10:37
 */
public enum OrderStatusEnum4StoreDisplayAndQuery {

    /**
     * 订单待商铺确认/待接单
     */
    UNRECEIVED((short) 111, "待接单"),
    /**
     * 待自提（已确认）
     */
    WAIT_SELF_LIFTING((short) 222, "待自提"),
    /**
     * 待退款
     */
    WAIT_REFUND((short) 333,"待退款"),
    /**
     * 待付款
     */
    WAIT_PAY((short) 444, "待付款"),
    /**
     * 已完成
     */
    FINISHED((short) 555, "已完成"),
    /**
     * 已取消
     */
    CANCELED((short) 666, "已取消"),
    /**
     * 已退款
     */
    REFUNDED((short) 777, "已退款");
    


    private short statusCode;
    private String statusDes;

    OrderStatusEnum4StoreDisplayAndQuery(short statusCode, String statusDes) {
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
