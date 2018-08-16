package com.winhxd.b2c.common.feign.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.pay.weixin.condition.PayRefundCondition;
import com.winhxd.b2c.pay.weixin.dto.PayRefundDTO;

import feign.hystrix.FallbackFactory;

@FeignClient(value = ServiceName.PAY_SERVICE, fallbackFactory = FinancialManagerServiceClientFallback.class)

public interface PayServiceClient {
	@PostMapping(value = "/6002/v1/orderRefund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<PayRefundDTO> orderRefund(@RequestBody PayRefundCondition condition);
	
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

	
}