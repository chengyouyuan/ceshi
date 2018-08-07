package com.winhxd.b2c.common.feign.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;

import feign.hystrix.FallbackFactory;

@FeignClient(value = ServiceName.ORDER_SERVICE, fallbackFactory = OrderServiceFallback.class)
public interface OrderServiceClient {
    @RequestMapping(value = "/order/v1/getOrderVo/", method = RequestMethod.GET)
    ResponseResult<OrderInfo> getOrderVo(@RequestParam("orderNo") String orderNo);
    
    @RequestMapping(value = "/order/451/v1/submitOrder/", method = RequestMethod.POST)
    ResponseResult<String> submitOrder(@RequestBody OrderCreateCondition orderCreateCondition);
    
    @RequestMapping(value = "/order/452/v1/queryStoreOrderSalesSummary/", method = RequestMethod.POST)
    ResponseResult<StoreOrderSalesSummaryVO> queryStoreOrderSalesSummary();
    
    @RequestMapping(value = "/order/453/v1/listOrder4Management/", method = RequestMethod.POST)
    ResponseResult<PagedList<OrderInfoDetailVO>> listOrder4Management(OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition);
    
    @RequestMapping(value = "/order/454/v1/getOrderDetail4Management/{orderNo}", method = RequestMethod.POST)
    ResponseResult<OrderInfoDetailVO4Management> getOrderDetail4Management(String orderNo);
}

@Component
class OrderServiceFallback implements OrderServiceClient, FallbackFactory<OrderServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceFallback.class);
    private Throwable throwable;

    public OrderServiceFallback() {
    }

    private OrderServiceFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ResponseResult<OrderInfo> getOrderVo(String orderNo) {
        logger.error("OrderServiceFallback -> getOrderVo", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public OrderServiceClient create(Throwable throwable) {
        return new OrderServiceFallback(throwable);
    }

    @Override
    public ResponseResult<String> submitOrder(OrderCreateCondition orderCreateCondition) {
        logger.error("OrderServiceFallback -> submitOrder", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<StoreOrderSalesSummaryVO> queryStoreOrderSalesSummary() {
        logger.error("OrderServiceFallback -> queryStoreOrderSalesSummary", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<OrderInfoDetailVO>> listOrder4Management(
            OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition) {
        logger.error("OrderServiceFallback -> listOrder4Management", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<OrderInfoDetailVO4Management> getOrderDetail4Management(String orderNo) {
        logger.error("OrderServiceFallback -> getOrderDetail4Management", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
