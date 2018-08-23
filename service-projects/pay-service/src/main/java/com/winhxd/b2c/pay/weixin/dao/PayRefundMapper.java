package com.winhxd.b2c.pay.weixin.dao;

import com.winhxd.b2c.pay.weixin.model.PayRefund;

/**
 * @author lizhonghua
 */
public interface PayRefundMapper {
    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入全部
     * @param record
     * @return
     */
    int insert(PayRefund record);

    /**
     * 插入有值的字段
     * @param record
     * @return
     */
    int insertSelective(PayRefund record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    PayRefund selectByPrimaryKey(Long id);

    /**
     * 根据主键更新有值的字段
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PayRefund record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(PayRefund record);

    /**
     * 根据退款单号查询
     * @param outRefundNo 退款单号
     * @return
     */
    PayRefund selectByOutRefundNo(String outRefundNo);

    /**
     * 根据商户流水单号查询
     * @param outTradeNo 商户流水号
     * @return
     */
    PayRefund selectByOutTradeNo(String outTradeNo);
}