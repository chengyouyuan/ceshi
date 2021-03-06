package com.winhxd.b2c.pay.weixin.dao;

import java.util.Date;

import com.winhxd.b2c.common.domain.pay.model.PayFinancialBillCount;

/**
 * @author yuluyuan
 */
public interface PayFinancialBillCountMapper {

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
    int insert(PayFinancialBillCount record);

    /**
     * 插入有值的字段
     * @param record
     * @return
     */
    int insertSelective(PayFinancialBillCount record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    PayFinancialBillCount selectByPrimaryKey(Long id);

    /**
     * 根据主键更新有值的字段
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PayFinancialBillCount record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(PayFinancialBillCount record);

    /**
     * 根据账单日期删除
     * @param billDate
     * @return
     */
    int deleteByBillDate(Date billDate);
}