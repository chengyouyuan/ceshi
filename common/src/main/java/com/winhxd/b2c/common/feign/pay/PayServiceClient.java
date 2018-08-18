package com.winhxd.b2c.common.feign.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;

import feign.hystrix.FallbackFactory;

@FeignClient(value = ServiceName.PAY_SERVICE, fallbackFactory = FinancialManagerServiceClientFallback.class)
public interface PayServiceClient {
	
	@PostMapping(value = "/6002/v1/orderRefund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<PayRefundVO> orderRefund(@RequestBody PayRefundCondition condition);
	
	@PostMapping(value = "/6001/v1/orderPay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<OrderPayVO> orderPay(@RequestBody PayPreOrderCondition condition);
	
	@PostMapping(value = "/6003/v1/transfersToChange", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<Integer> transfersToChange(@RequestBody PayTransfersToWxChangeCondition condition);
	
	@PostMapping(value = "/6004/v1/transfersToBank", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<Integer> transfersToBank(@RequestBody PayTransfersToWxBankCondition condition);
	
}
class PayServiceClientFallback implements PayServiceClient, FallbackFactory<PayServiceClient>{
	private static final Logger logger = LoggerFactory.getLogger(FinancialManagerServiceClientFallback.class);
    private Throwable throwable;

    public PayServiceClientFallback() {
    	
    }

    private PayServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }
	@Override
	public PayServiceClient create(Throwable arg0) {
		return new PayServiceClientFallback(throwable);
	}

	@Override
	public ResponseResult<PayRefundVO> orderRefund(PayRefundCondition condition) {
		logger.error("PayServiceClientFallback -> orderRefund", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<OrderPayVO> orderPay(PayPreOrderCondition condition) {
		logger.error("PayServiceClientFallback -> orderPay", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<Integer> transfersToChange(PayTransfersToWxChangeCondition condition) {
		logger.error("PayServiceClientFallback -> transfersToChange", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<Integer> transfersToBank(PayTransfersToWxBankCondition condition) {
		logger.error("PayServiceClientFallback -> transfersToBank", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	
}