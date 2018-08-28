package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayRefundPayment;

public interface PayRefundPaymentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayRefundPayment record);

    int insertSelective(PayRefundPayment record);

    PayRefundPayment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRefundPayment record);

    int updateByPrimaryKey(PayRefundPayment record);

    int updateByRefundTransactionNoSelective(PayRefundPayment record);

    /**
     * @author liuhanning
     * @date  2018年8月27日 下午8:37:31
     * @Description 根据退款流水号查询 退款
     * @param id
     * @return
     */
    PayRefundPayment selectByRefundTransactionNo(String refundTransactionNo);
}