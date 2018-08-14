package com.winhxd.b2c.common.feign.order;

import java.util.List;

import com.winhxd.b2c.common.domain.order.condition.OrderRefundCallbackCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.condition.StoreOrderSalesSummaryCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;

import feign.hystrix.FallbackFactory;

@FeignClient(value = ServiceName.ORDER_SERVICE, fallbackFactory = OrderServiceFallback.class)
public interface OrderServiceClient {
    @RequestMapping(value = "/order/4051/v1/submitOrder/", method = RequestMethod.POST)
    ResponseResult<String> submitOrder(@RequestBody OrderCreateCondition orderCreateCondition);

    /**
     * @author wangbin
     * @date  2018年8月11日 下午5:11:54
     * @Description 
     * @return
     * @deprecated 使用queryStoreOrderSalesSummaryByDateTimePeriod
     */
    @RequestMapping(value = "/order/4052/v1/queryStoreOrderSalesSummary/", method = RequestMethod.POST)
    @Deprecated()
    ResponseResult<StoreOrderSalesSummaryVO> queryStoreOrderSalesSummary();
    
    @RequestMapping(value = "/order/4053/v1/listOrder4Management/", method = RequestMethod.POST)
    ResponseResult<PagedList<OrderInfoDetailVO>> listOrder4Management(@RequestBody OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition);
    
    @RequestMapping(value = "/order/4054/v1/getOrderDetail4Management/{orderNo}", method = RequestMethod.POST)
    ResponseResult<OrderInfoDetailVO4Management> getOrderDetail4Management(@PathVariable(value = "orderNo") String orderNo);

    @RequestMapping(value = "/order/4055/v1/queryStoreOrderSalesSummaryByDateTimePeriod/", method = RequestMethod.POST)
    ResponseResult<StoreOrderSalesSummaryVO> queryStoreOrderSalesSummaryByDateTimePeriod(@RequestBody StoreOrderSalesSummaryCondition storeOrderSalesSummaryCondition);
    
    @RequestMapping(value = "/order/4056/v1/listOrderWithNoPage/", method = RequestMethod.POST)
    ResponseResult<List<OrderInfoDetailVO>> listOrderWithNoPage(@RequestBody OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition);

    /**
     * 申请退款回调（设置订单状态为退款中）
     *
     * @param orderRefundCallbackCondition 入参
     * @return 是否成功
     */
    @RequestMapping(value = "/order/4057/v1/updateOrderRefundCallback/", method = RequestMethod.POST)
    ResponseResult<Boolean> updateOrderRefundCallback(@RequestBody OrderRefundCallbackCondition orderRefundCallbackCondition);


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

    @Override
    public ResponseResult<StoreOrderSalesSummaryVO> queryStoreOrderSalesSummaryByDateTimePeriod(StoreOrderSalesSummaryCondition storeOrderSalesSummaryCondition) {
        logger.error("OrderServiceFallback -> queryStoreOrderSalesSummaryByDateTimePeriod", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<List<OrderInfoDetailVO>> listOrderWithNoPage(
            OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition) {
        logger.error("OrderServiceFallback -> listOrderWithNoPage", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    /**
     * 申请退款回调（设置订单状态为退款中）
     *
     * @param orderRefundCallbackCondition 入参
     * @return 是否成功
     */
    @Override
    public ResponseResult<Boolean> updateOrderRefundCallback(OrderRefundCallbackCondition orderRefundCallbackCondition) {
        logger.error("OrderServiceFallback -> updateOrderRefundCallback", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
