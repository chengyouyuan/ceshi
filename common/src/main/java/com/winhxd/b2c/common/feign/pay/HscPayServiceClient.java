package com.winhxd.b2c.common.feign.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.model.PayBill;

import feign.hystrix.FallbackFactory;

@FeignClient(value = ServiceName.HSC_PAY_SERVICE, fallbackFactory = HscPayServiceClientFallback.class)
public interface HscPayServiceClient {
	/**
	 * 逆向物流 微信支付回调 调用接口
	 * @author likai
	 * @date 2018年9月5日 上午11:06:27
	 * @param condition
	 * @return
	 */
	@PostMapping(value = "/pay/6525/v1/callbackOrderPay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseResult<Boolean> callbackOrderPay(@RequestBody PayBill condition);

}

@Component
class HscPayServiceClientFallback implements HscPayServiceClient, FallbackFactory<HscPayServiceClient>{
	private static final Logger logger = LoggerFactory.getLogger(FinancialManagerServiceClientFallback.class);
    private Throwable throwable;

    public HscPayServiceClientFallback() {
    	
    }

    private HscPayServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }
	@Override
	public HscPayServiceClient create(Throwable arg0) {
		return new HscPayServiceClientFallback(throwable);
	}

	@Override
	public ResponseResult<Boolean> callbackOrderPay(PayBill condition) {
		logger.error("HscPayServiceClientFallback -> callbackOrderPay{}", throwable);
      return new ResponseResult<>(BusinessCode.CODE_1001);
	}



	
}