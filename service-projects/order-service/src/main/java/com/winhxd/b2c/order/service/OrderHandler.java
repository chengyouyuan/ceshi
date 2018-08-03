package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.order.model.OrderInfo;

public interface OrderHandler {

    /**
     * 订单创建前相关业务逻辑
     * @author wangbin
     * @date  2018年8月3日 下午2:29:39
     * @param orderInfo
     */
    void orderInfoBeforeCreateProcess(OrderInfo orderInfo);
    /**
     * 订单创建后相关业务逻辑
     * @author wangbin
     * @date  2018年8月3日 下午2:29:39
     * @param orderInfo
     */
    void orderInfoAfterCreateProcess(OrderInfo orderInfo);
    
    /**
     * 订单成功提交后相关业务逻辑,比如发送消息等相关边缘操作
     * @author wangbin
     * @date  2018年8月3日 下午4:38:24
     * @param orderInfo
     */
    void orderInfoAfterCreateSuccessProcess(OrderInfo orderInfo);
}
