package com.winhxd.b2c.pay.dao;

import java.util.List;

import com.winhxd.b2c.common.domain.pay.model.PayWithdrawalsType;

public interface PayWithdrawalsTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayWithdrawalsType record);

    int insertSelective(PayWithdrawalsType record);

    PayWithdrawalsType selectByPrimaryKey(Long id);
    
    /**查询所有的体现方式*/
    List<PayWithdrawalsType> selectAll();

    int updateByPrimaryKeySelective(PayWithdrawalsType record);

    int updateByPrimaryKey(PayWithdrawalsType record);
}