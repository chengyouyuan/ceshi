package com.winhxd.b2c.pay.service;

import com.winhxd.b2c.common.domain.pay.condition.PayStoreWalletCondition;

public interface PayStoreWalletService {
	
	void savePayStoreWallet(PayStoreWalletCondition condition);
}
