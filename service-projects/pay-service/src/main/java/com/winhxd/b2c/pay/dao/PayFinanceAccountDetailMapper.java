package com.winhxd.b2c.pay.dao;

import java.util.List;

import com.winhxd.b2c.common.domain.pay.condition.PayFinanceAccountDetailCondition;
import com.winhxd.b2c.common.domain.pay.model.PayFinanceAccountDetail;
import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;

public interface PayFinanceAccountDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayFinanceAccountDetail record);

    int insertSelective(PayFinanceAccountDetail record);

    PayFinanceAccountDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayFinanceAccountDetail record);

    int updateByPrimaryKey(PayFinanceAccountDetail record);

	PayFinanceAccountDetailVO selectByStoreId(Long storeId);

	List<PayFinanceAccountDetailVO> selectTotalInOutMoney(Long storeId);

	List<PayFinanceAccountDetailVO> selectTodayInMoney(Long storeId);

	List<PayFinanceAccountDetailVO> selectTodayOutMoney(Long storeId);

	PayFinanceAccountDetailVO selectCouponUsedMoney(Long storeId);

	PayFinanceAccountDetailVO selectTodayCouponUsedMoney(Long storeId);

	List<PayFinanceAccountDetailVO> selectFinancialInDetail(PayFinanceAccountDetailCondition condition);

	List<PayFinanceAccountDetailVO> selectFinancialOutDetail(PayFinanceAccountDetailCondition condition);
}