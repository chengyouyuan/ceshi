package com.winhxd.b2c.common.feign.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;

import com.winhxd.b2c.common.constant.ServiceName;

import feign.hystrix.FallbackFactory;

@FeignClient(value = ServiceName.PAY_SERVICE, fallbackFactory = FinancialManagerServiceClientFallback.class)

public interface PayServiceClient {

	
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