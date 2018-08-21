package com.winhxd.b2c.pay.weixin.dao;

import java.util.Date;

import com.winhxd.b2c.common.domain.pay.model.PayStatement;

public interface PayStatementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStatement record);

    int insertSelective(PayStatement record);

    PayStatement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStatement record);

    int updateByPrimaryKey(PayStatement record);

    int deleteByBillDate(Date billDate);

	/**
	 * @Description 根据订单号查询成功支付的对账单
	 * @author yuluyuan
	 * @date 2018年8月21日 下午3:51:11
	 * @param outOrderNo
	 * @return
	 */
	PayStatement selectByOutOrderNo(String outOrderNo);
}