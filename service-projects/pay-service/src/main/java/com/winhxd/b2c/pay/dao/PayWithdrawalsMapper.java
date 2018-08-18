package com.winhxd.b2c.pay.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.pay.condition.PayWithdrawalsListCondition;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawals;
import com.winhxd.b2c.common.domain.pay.vo.PayStoreUserInfoVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayWithdrawalsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayWithdrawals record);

    int insertSelective(PayWithdrawals record);

    PayWithdrawals selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayWithdrawals record);

    int updateByPrimaryKey(PayWithdrawals record);

    List<PayWithdrawalsVO> getPayWithdrawalsByStoreId(@Param("storeId") Long storeId);

    Page<PayWithdrawalsVO> selectPayWithdrawalsListByCondition(PayWithdrawalsListCondition condition);

    List<PayStoreUserInfoVO> getPayStoreUserInfo(@Param("storeId") Long storeId);

    List<PayStoreUserInfoVO> getStorBankCardInfo(@Param("storeId") Long storeId);

    int updateByWithdrawalsNoSelective(PayWithdrawals payWithdrawals);
}