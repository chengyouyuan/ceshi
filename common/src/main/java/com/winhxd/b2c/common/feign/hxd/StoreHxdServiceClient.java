package com.winhxd.b2c.common.feign.hxd;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;

import feign.hystrix.FallbackFactory;

/**
 * @author wufuyun
 * @date 2018年8月6日 下午4:49:30
 * @Description 调用惠下单服务
 */
@FeignClient(name = "RETAIL-REST-SERVICE", path = "/restapi", fallbackFactory = StoreHxdServiceClientFallBack.class)
public interface StoreHxdServiceClient {

    @RequestMapping(value = "/hxdStore/getStoreUserInfo", method = RequestMethod.GET)
    ResponseResult<Map<String, Object>> getStoreUserInfo(@RequestParam("storeMobile") String storeMobile, 
    		@RequestParam("storePassword") String storePassword); 
    @RequestMapping(value = "/hxdStore/getStoreUserInfoByCustomerId", method = RequestMethod.GET)
    ResponseResult<Map<String, Object>> getStoreUserInfoByCustomerId(@RequestParam("customerId") Long customerId); 

    @RequestMapping(value = "/hxdStore/getStorePerfectInfo/", method = RequestMethod.GET)
    ResponseResult<List<String>> getStorePerfectInfo(@RequestParam("storeId") String storeId);

    /**
     * 功能描述:获得门店在惠下单购买过商品sku
     * @param customerId
     * @return
     * @auther lvsen
     * @date 2018/8/7 20:40
     */
    @RequestMapping(value = "/hxdStore/getStoreBuyedProdSku/", method = RequestMethod.GET)
    ResponseResult<List<String>> getStoreBuyedProdSku(@RequestParam("customerId") String customerId);

    @RequestMapping(value = "/hxdStore/getStoreBaseInfo/", method = RequestMethod.GET)
    ResponseResult<Object> getStoreBaseInfo(@RequestParam("storeId") String storeId);

}

@Component
class StoreHxdServiceClientFallBack implements StoreHxdServiceClient, FallbackFactory<StoreHxdServiceClient> {

    Throwable throwable;

    Logger logger = LoggerFactory.getLogger(StoreHxdServiceClientFallBack.class);

    @Override
    public StoreHxdServiceClient create(Throwable throwable) {
        this.throwable = throwable;
        return new StoreHxdServiceClientFallBack();
    }

    @Override
    public ResponseResult<Map<String, Object>> getStoreUserInfo(@RequestParam("storeMobile") String storeMobile, @RequestParam("storePassword") String storePassword) {
        logger.error("惠下单 StoreHxdServiceClientFallBack -> getStotrUserInfo报错，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<List<String>> getStorePerfectInfo(String storeId) {
        logger.error("StoreHxdServiceClient -> getStorePerfectInfo", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<List<String>> getStoreBuyedProdSku(String storeId) {
        logger.error("StoreHxdServiceClient -> getStoreBuyedProdSku", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Object> getStoreBaseInfo(String storeId) {
        logger.error("StoreHxdServiceClient -> getStoreBaseInfo", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

	@Override
	public ResponseResult<Map<String, Object>> getStoreUserInfoByCustomerId(Long customerId) {
		  logger.error("惠下单 StoreHxdServiceClientFallBack -> getStoreUserInfoByCustomerId报错，错误信息为{}", throwable);
	        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

}

