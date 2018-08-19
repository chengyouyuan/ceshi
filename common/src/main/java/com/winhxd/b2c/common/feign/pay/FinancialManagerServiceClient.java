package com.winhxd.b2c.common.feign.pay;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.FinancialManagerCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ServiceName.PAY_SERVICE, fallbackFactory = FinancialManagerServiceClientFallback.class)
public interface FinancialManagerServiceClient {
	/**出入帐汇总查询*/
	@RequestMapping(value = "/pay/6101/v1/queryStoreFinancialSummary",method = RequestMethod.POST)
    ResponseResult<PayFinanceAccountDetailVO> queryStoreFinancialSummary(@RequestBody FinancialManagerCondition condition);
	
	/**财务入账明细*//*
	@PostMapping("/pay/61002/v1/queryFinancialInDetail")
	ResponseResult<PagedList<OrderInfoFinancialInDetailVO>> queryFinancialInDetail(@RequestBody OrderInfoFinancialInDetailCondition condition);
	
	*//**财务出账明细*//*
	@PostMapping("/pay/61003/v1/queryFinancialOutDetail")
	ResponseResult<PagedList<OrderInfoFinancialOutDetailVO>> queryFinancialOutDetail(@RequestBody OrderInfoFinancialOutDetailCondition condition);
	*//**公司入账明细*//*
	// 待定
*/
}
@Component
class FinancialManagerServiceClientFallback implements FinancialManagerServiceClient, FallbackFactory<FinancialManagerServiceClient>{
	private static final Logger logger = LoggerFactory.getLogger(FinancialManagerServiceClientFallback.class);
    private Throwable throwable;

    public FinancialManagerServiceClientFallback() {
    	
    }

    private FinancialManagerServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }
	@Override
	public FinancialManagerServiceClient create(Throwable arg0) {
		return new FinancialManagerServiceClientFallback(throwable);
	}

	@Override
	public ResponseResult<PayFinanceAccountDetailVO> queryStoreFinancialSummary(FinancialManagerCondition condition) {
		logger.error("FinancialManagerServiceClientFallback -> queryStoreFinancialSummary", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}
/*
	@Override
	public ResponseResult<PagedList<OrderInfoFinancialInDetailVO>> queryFinancialInDetail(@RequestBody OrderInfoFinancialInDetailCondition condition) {
		logger.error("FinancialManagerServiceClientFallback -> queryFinancialDetail", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<PagedList<OrderInfoFinancialOutDetailVO>> queryFinancialOutDetail(@RequestBody OrderInfoFinancialOutDetailCondition condition) {
		logger.error("FinancialManagerServiceClientFallback -> queryFinancialOutDetail", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}*/
	
}