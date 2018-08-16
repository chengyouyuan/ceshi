package com.winhxd.b2c.common.feign.order;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.ShopCartProductCondition;
import com.winhxd.b2c.common.domain.order.vo.ShopCartProductVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author caiyulong
 */
@FeignClient(value = ServiceName.ORDER_SERVICE, fallbackFactory = ShopCartServiceClientFallback.class)
public interface ShopCartServiceClient {

    @RequestMapping(value = "/order/4070/v1/queryShopCartBySelective", method = RequestMethod.POST)
    ResponseResult<List<ShopCartProductVO>> queryShopCartBySelective(@RequestBody ShopCartProductCondition condition);
}
@Component
class ShopCartServiceClientFallback implements ShopCartServiceClient, FallbackFactory<ShopCartServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(ShopCartServiceClientFallback.class);
    private Throwable throwable;

    public ShopCartServiceClientFallback() {
    }

    private ShopCartServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ShopCartServiceClient create(Throwable throwable) {
        return new ShopCartServiceClientFallback(throwable);
    }

    @Override
    public ResponseResult<List<ShopCartProductVO>> queryShopCartBySelective(ShopCartProductCondition condition) {
        logger.error("OrderServiceFallback -> queryShopCartBySelective", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
