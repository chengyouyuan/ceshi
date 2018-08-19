package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayOrderPayment;

public interface PayOrderPaymentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayOrderPayment record);

    int insertSelective(PayOrderPayment record);

    PayOrderPayment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayOrderPayment record);

    int updateByPrimaryKey(PayOrderPayment record);

    /**
     * 根据支付流水号查询-lizhonghua-2018年8月15日11:29:27
     * @param orderPaymentNo
     * @return
     */
    PayOrderPayment selectByOrderPaymentNo(String orderPaymentNo);
    
    /**
     * @author liuhanning
     * @date  2018年8月18日 下午6:41:43
     * @Description 根据订单交易号  更新流水信息
     * @param record
     * @return
     */
    int updateByOrderTransactionNoSelective(PayOrderPayment record);
}