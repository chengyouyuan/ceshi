package com.winhxd.b2c.common.feign.order;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.vo.OrderVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceName.ORDER_SERVICE, fallbackFactory = OrderServiceFallback.class)
public interface OrderServiceClient {
    @RequestMapping(value = "/order/v1/getOrderVo/", method = RequestMethod.GET)
    ResponseResult<OrderVO> getOrderVo(@RequestParam("orderNo") String orderNo);
}

class OrderServiceFallback implements OrderServiceClient, FallbackFactory<OrderServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceFallback.class);
    private Throwable throwable;

    public OrderServiceFallback() {
    }

    private OrderServiceFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ResponseResult<OrderVO> getOrderVo(String orderNo) {
        logger.error("OrderServiceFallback -> getOrderVo", throwable);
        return new ResponseResult<>(BusinessCode.CODE_401);
    }

    @Override
    public OrderServiceClient create(Throwable throwable) {
        return new OrderServiceFallback(throwable);
    }
}
