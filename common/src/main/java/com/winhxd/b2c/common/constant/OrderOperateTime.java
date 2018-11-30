package com.winhxd.b2c.common.constant;

public class OrderOperateTime {

    /**
     * 订单门店确认时间范围：24小时
     */
    public static final int ORDER_NEED_RECEIVE_TIME_BY_MILLISECONDS = 24 * 60 * 60 * 1000;

//    /**
//     * 客户自提 时间范围：36小时
//     */
//    public static final int ORDER_NEED_PICKUP_TIME_BY_MILLISECONDS = 36 * 60 * 60 * 1000;


    /**
     * 客户自提 时间范围：36小时
     */
    public static final int ORDER_NEED_PICKUP_TIME_BY_MILLISECONDS = 3 * 60 * 1000;


    /**
     * 客户付款 时间范围：24小时
     */
    public static final int ORDER_NEED_PAY_TIME_BY_MILLISECONDS = 24 * 60 * 60 * 1000;
}
