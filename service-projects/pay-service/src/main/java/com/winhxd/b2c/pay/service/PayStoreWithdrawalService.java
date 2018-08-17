package com.winhxd.b2c.pay.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreApplyWithDrawCondition;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawalsType;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalPageVO;

/**
 * @Author zhanghuan
 * @Date 2018/8/14 17:03
 * @Description
 **/
public interface PayStoreWithdrawalService {
	/**返回所有的提现方式
	 * */
	ResponseResult<List<PayWithdrawalsType>> getAllWithdrawalType();
	/**保存用户提现信息*/
	ResponseResult<Integer> saveStorWithdrawalInfo(@RequestBody PayStoreApplyWithDrawCondition condition);
	/**进入提现页面*/
	ResponseResult<PayWithdrawalPageVO> showPayWithdrawalDetail(PayStoreApplyWithDrawCondition condition);
}
