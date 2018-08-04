package com.winhxd.b2c.common.feign.store;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: 门店服务接口
 * @author chengyy
 * @date 2018/8/3 10:18
 */
@FeignClient(value = ServiceName.STORE_SERVICE,fallbackFactory = StoreServiceClientFallBack.class)
public interface StoreServiceClient {
    /**
     * @author chengyy
     * @date 2018/8/3 10:32
     * @Description 门店绑定用户
     * @param  customerId 用户id主键
     * @Param storeUserId  门店id主键
     * @return 无
     */
    @RequestMapping(value = "/store/2001/v1/bindCustomer",method = RequestMethod.GET)
    ResponseResult<Void> bindCustomer(@RequestParam("customerId")Long customerId,@RequestParam("storeUserId")Long storeUserId);


}
/**
 * @Description: 熔断回调
 * @author chengyy
 * @date 2018/8/3 10:43
 */
class StoreServiceClientFallBack implements StoreServiceClient, FallbackFactory<StoreServiceClient> {

    Throwable throwable;

    Logger logger = LoggerFactory.getLogger(StoreServiceClientFallBack.class);

    @Override
    public StoreServiceClient create(Throwable throwable) {
        this.throwable = throwable;
        return new StoreServiceClientFallBack();
    }

    @Override
    public ResponseResult<Void> bindCustomer(Long customerId,Long storeUserId) {
        logger.error("StoreServiceClientFallBack -> bindCustomer报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

}
