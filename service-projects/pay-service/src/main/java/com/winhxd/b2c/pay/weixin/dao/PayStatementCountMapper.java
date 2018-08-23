package com.winhxd.b2c.pay.weixin.dao;

import java.util.Date;

import com.winhxd.b2c.common.domain.pay.model.PayStatementCount;

/**
 * @author yuluyuan
 */
public interface PayStatementCountMapper {
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
    int insert(PayStatementCount record);

    /**
     * 插入有值的字段
     * @param record
     * @return
     */
    int insertSelective(PayStatementCount record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    PayStatementCount selectByPrimaryKey(Long id);

    /**
     * 根据主键更新有值的字段
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PayStatementCount record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(PayStatementCount record);

    /**
     * 根据账单日期删除
     * @param billDate
     * @return
     */
    int deleteByBillDate(Date billDate);
}