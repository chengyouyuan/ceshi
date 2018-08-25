package com.winhxd.b2c.pay.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.winhxd.b2c.common.domain.pay.condition.CalculationCmmsAmtCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreApplyWithDrawCondition;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawalsType;
import com.winhxd.b2c.common.domain.pay.vo.CalculationCmmsAmtVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalPageVO;

/**
 * @Author zhanghuan
 * @Date 2018/8/14 17:03
 * @Description
 **/
public interface PayStoreWithdrawalService {
	/**返回所有的提现方式
	 * */
	List<PayWithdrawalsType> getAllWithdrawalType();
	/**保存用户提现信息*/
	void saveStorWithdrawalInfo(@RequestBody PayStoreApplyWithDrawCondition condition);
	/**进入提现页面*/
	PayWithdrawalPageVO showPayWithdrawalDetail(PayStoreApplyWithDrawCondition condition);
	
	/**
	 * @author liuhanning
	 * @date  2018年8月21日 下午9:27:00
	 * @Description 提现计算费率
	 * @return
	 */
	CalculationCmmsAmtVO calculationCmmsAmt(CalculationCmmsAmtCondition condition);
}
