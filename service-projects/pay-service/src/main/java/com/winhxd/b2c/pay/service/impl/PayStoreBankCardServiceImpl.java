package com.winhxd.b2c.pay.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.pay.condition.StoreBankCardCondition;
import com.winhxd.b2c.common.domain.pay.model.StoreBankCard;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankCardVO;
import com.winhxd.b2c.pay.dao.StoreBankCardMapper;
import com.winhxd.b2c.pay.service.PayStoreBankCardService;

@Service
public class PayStoreBankCardServiceImpl implements PayStoreBankCardService {
	
	@Resource
	private StoreBankCardMapper storeBankCardMapper;

	@Override
	public StoreBankCardVO findStoreBankCardInfo(StoreBankCardCondition condition) {
		StoreBankCardVO storeBankCard = new StoreBankCardVO();
		storeBankCard = storeBankCardMapper.selectStorBankCardInfo(condition);
		return storeBankCard;
	}

	public int saveStoreBankCard(StoreBankCard condition) {
		int res = storeBankCardMapper.insertStoreBankCardinfo(condition);
		return res;
	}

}
