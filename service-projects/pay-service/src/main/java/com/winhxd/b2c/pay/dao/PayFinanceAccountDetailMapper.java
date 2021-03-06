package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.condition.OrderInfoFinancialInDetailCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderInfoFinancialOutDetailCondition;
import com.winhxd.b2c.common.domain.pay.model.PayFinanceAccountDetail;
import com.winhxd.b2c.common.domain.pay.model.PayFinancialSummary;
import com.winhxd.b2c.common.domain.pay.vo.OrderInfoFinancialInDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderInfoFinancialOutDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PayFinanceAccountDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayFinanceAccountDetail record);

    int insertSelective(PayFinanceAccountDetail record);

    PayFinanceAccountDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayFinanceAccountDetail record);

    int updateByPrimaryKey(PayFinanceAccountDetail record);

//	PayFinanceAccountDetailVO selectByStoreId(Long storeId);

//	List<PayFinanceAccountDetailVO> selectTotalInOutMoney(Long storeId);
//
//	List<PayFinanceAccountDetailVO> selectTodayInMoney(Long storeId);
//
//	List<PayFinanceAccountDetailVO> selectTodayOutMoney(Long storeId);

//	PayFinanceAccountDetailVO selectCouponUsedMoney(Long storeId);
//
//	PayFinanceAccountDetailVO selectTodayCouponUsedMoney(Long storeId);

//	List<OrderInfoFinancialInDetailVO> selectFinancialInDetail(OrderInfoFinancialInDetailCondition condition);

//	List<OrderInfoFinancialOutDetailVO> selectFinancialOutDetail(OrderInfoFinancialOutDetailCondition condition);

	/**
	 * 提现数据
	 * @return
	 */
	PayFinancialSummary getWithdrawals(@Param("isToday") String isToday);


	/**
	 * 退款数据
	 * @return
	 */
	PayFinancialSummary getRefund(@Param("isToday") String isToday);

	/**
	 * 进账数据及优惠券抵用金额
	 * @param type
	 * @return
	 */
    BigDecimal getIncome(@Param("type")String type);

	/**
	 * 营收金额
	 * @return
	 */
    BigDecimal getRevenueMoney();

	List<PayFinanceAccountDetail> selectByExample(PayFinanceAccountDetail condition);

	/**
	 * 公司补充入账
	 * @return
	 */
    BigDecimal getCompanySupplementInMoney();
}