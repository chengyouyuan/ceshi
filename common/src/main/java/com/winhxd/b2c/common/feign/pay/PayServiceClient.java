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
import com.winhxd.b2c.common.domain.pay.condition.OrderIsPayCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;

import feign.hystrix.FallbackFactory;

@FeignClient(value = ServiceName.PAY_SERVICE, fallbackFactory = PayServiceClientFallback.class)
public interface PayServiceClient {
	
//	/**
//	 * @author liuhanning
//	 * @date  2018年8月20日 下午1:19:20
//	 * @Description 退款
//	 * @param condition
//	 * @return
//	 */
//	@PostMapping(value = "/pay/6002/v1/orderRefund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResponseResult<PayRefundVO> orderRefund(@RequestBody PayRefundCondition condition);
	
	/**
	 * @author liuhanning
	 * @date  2018年8月20日 下午1:19:26
	 * @Description 支付
	 * @param condition
	 * @return
	 */
	@PostMapping(value = "/pay/6001/v1/orderPay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<OrderPayVO> orderPay(@RequestBody PayPreOrderCondition condition);
	
	@PostMapping(value = "/pay/6003/v1/transfersToChange", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<Integer> transfersToChange(@RequestBody PayTransfersToWxChangeCondition condition);
	
	@PostMapping(value = "/pay/6004/v1/transfersToBank", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<Integer> transfersToBank(@RequestBody PayTransfersToWxBankCondition condition);
	
	/**
	 * @author liuhanning
	 * @date  2018年8月20日 下午1:19:39
	 * @Description 订单是否支付过
	 * @param condition
	 * @return
	 */
	@PostMapping(value = "/pay/6016/v1/orderIsPay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<Boolean> orderIsPay(@RequestBody OrderIsPayCondition condition);

	/**
	 * 轮询确认转账到银行卡记录状态
	 * @Author yindanqing
	 * @Date 2018-8-22 12:50:29
	 * @return 更新状态计数
	 */
	@PostMapping(value = "/pay/6017/v1/confirmTransferToBankStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseResult<Integer> confirmTransferToBankStatus() throws Exception;

}

@Component
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

//	@Override
//	public ResponseResult<PayRefundVO> orderRefund(PayRefundCondition condition) {
//		logger.error("PayServiceClientFallback -> orderRefund{}", throwable);
//        return new ResponseResult<>(BusinessCode.CODE_1001);
//	}

	@Override
	public ResponseResult<OrderPayVO> orderPay(PayPreOrderCondition condition) {
		logger.error("PayServiceClientFallback -> orderPay{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<Integer> transfersToChange(PayTransfersToWxChangeCondition condition) {
		logger.error("PayServiceClientFallback -> transfersToChange{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<Integer> transfersToBank(PayTransfersToWxBankCondition condition) {
		logger.error("PayServiceClientFallback -> transfersToBank{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<Boolean> orderIsPay(OrderIsPayCondition condition) {
		logger.error("PayServiceClientFallback -> orderIsPay{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<Integer> confirmTransferToBankStatus(){
		logger.error("PayServiceClientFallback -> confirmTransferToBankStatus{}", throwable);
		ResponseResult<Integer> result = new ResponseResult<Integer>();
		result.setCode(BusinessCode.CODE_1001);
		return result;
	}

	
}