package com.winhxd.b2c.pay.weixin.dao;

import java.util.Date;

import com.winhxd.b2c.common.domain.pay.model.PayStatement;

/**
 * @author yuluyuan
 */
public interface PayStatementMapper {
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
    int insert(PayStatement record);

	/**
	 * 插入有值的字段
	 * @param record
	 * @return
	 */
    int insertSelective(PayStatement record);

	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
    PayStatement selectByPrimaryKey(Long id);

	/**
	 * 根据主键更新有值的字段
	 * @param record
	 * @return
	 */
    int updateByPrimaryKeySelective(PayStatement record);

	/**
	 * 根据主键更新
	 * @param record
	 * @return
	 */
    int updateByPrimaryKey(PayStatement record);

	/**
	 * 根据账单日期删除
	 * @param billDate
	 * @return
	 */
	int deleteByBillDate(Date billDate);

	/**
	 * 根据订单号查询成功支付的对账单
	 * @Description 根据订单号查询成功支付的对账单
	 * @author yuluyuan
	 * @date 2018年8月21日 下午3:51:11
	 * @param outOrderNo
	 * @return
	 */
	PayStatement selectByOutOrderNo(String outOrderNo);
}