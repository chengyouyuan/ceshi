package com.winhxd.b2c.pay.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.pay.condition.OrderInfoFinancialInDetailCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderInfoFinancialOutDetailCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderInfoFinancialInDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderInfoFinancialOutDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;

public interface PayFinancialManagerService {
	/**出入帐汇总查询*/
	PayFinanceAccountDetailVO findFinanceAccountDetail(Long storeId);
	
	/**财务入账明细*/
	PagedList<OrderInfoFinancialInDetailVO> queryFinancialInDetail(@RequestBody OrderInfoFinancialInDetailCondition condition);
	
	/**财务出账明细*/
	PagedList<OrderInfoFinancialOutDetailVO> queryFinancialOutDetail(@RequestBody OrderInfoFinancialOutDetailCondition condition);

	/**
	 * 资金汇总接口
	 * @return
	 */
	PayFinanceAccountDetailVO findStoreFinancialSummary();
}
