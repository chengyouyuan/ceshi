package com.winhxd.b2c.pay.service;

import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;

public interface PayFinancialManagerService {
	/**出入帐汇总查询*/
	PayFinanceAccountDetailVO findFinanceAccountDetail(Long storeId);

	/**
	 * 资金汇总接口
	 * @return
	 */
	PayFinanceAccountDetailVO findStoreFinancialSummary();
}
