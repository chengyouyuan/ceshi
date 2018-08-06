package com.winhxd.b2c.common.feign.hxd;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.feign.hxd.StoreHxdServiceClient.StoreServiceClientFallBack;

import feign.hystrix.FallbackFactory;
/**
 * @author wufuyun
 * @date  2018年8月6日 下午4:49:30
 * @Description 调用惠下单服务
 * @version
 */
@FeignClient(name = "RETAIL-REST-SERVICE", path = "/restapi", fallbackFactory = StoreServiceClientFallBack.class)
public interface StoreHxdServiceClient {

	@RequestMapping(value = "/store/getStotrUserInfo", method = RequestMethod.GET)
    ResponseResult<Map<String,Object>> getStotrUserInfo(Map<String, Object> request);
	
	class StoreServiceClientFallBack implements StoreHxdServiceClient, FallbackFactory<StoreHxdServiceClient> {

	    Throwable throwable;

	    Logger logger = LoggerFactory.getLogger(StoreServiceClientFallBack.class);
		@Override
		public StoreHxdServiceClient create(Throwable throwable) {
	        this.throwable = throwable;
	        return new StoreServiceClientFallBack();
		}
		@Override
		public ResponseResult<Map<String, Object>> getStotrUserInfo(Map<String, Object> request) {
			 logger.error("惠下单 StoreHxdServiceClientFallBack -> getStotrUserInfo报错，错误信息为{}", throwable);
		     return new ResponseResult<>(BusinessCode.CODE_1001);
		}
		
	}
}
