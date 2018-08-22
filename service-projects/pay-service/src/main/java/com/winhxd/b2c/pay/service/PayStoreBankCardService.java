package com.winhxd.b2c.pay.service;

import com.winhxd.b2c.common.domain.pay.condition.StoreBankCardCondition;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankCardVO;

/**
 * B端查询银行卡信息接口
 *
 * @author zhanghuan
 * @date 2018年8月10日 
 */
public interface PayStoreBankCardService {
	
	/** B端查询银行卡信息*/
	StoreBankCardVO findStoreBankCardInfo(StoreBankCardCondition condition);
	
	/**B端绑定银行卡*/
	int saveStoreBankCard(StoreBankCardCondition condition);
}
