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
    
    /**
     * 订单支付后相关业务逻辑
     * @author wangbin
     * @date  2018年8月6日 下午1:32:16
     * @param orderInfo
     */
    void orderFinishPayProcess(OrderInfo orderInfo);
    
    /**
     * 订单接单业务处理逻辑
     * @author wangbin
     * @date  2018年8月8日 上午11:09:09
     * @param orderInso
     */
    void orderInfoConfirmProcess(OrderInfo orderInfo);
    
    /**
     * 订单支付成功提交后相关业务逻辑,比如发送消息等相关边缘操作
     * @author wangbin
     * @date  2018年8月3日 下午4:38:24
     * @param orderInfo
     */
    void orderInfoAfterPaySuccessProcess(OrderInfo orderInfo);
    
    /**
     * 订单确认完成 提交后相关业务逻辑,比如发送消息等相关边缘操作
     * @author wangbin
     * @date  2018年8月10日 下午6:05:13
     * @Description 
     * @param orderInfo
     */
    void orderInfoAfterConfirmSuccessProcess(OrderInfo orderInfo);
}
