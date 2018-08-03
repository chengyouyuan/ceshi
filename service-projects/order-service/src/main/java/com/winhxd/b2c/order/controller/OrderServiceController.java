package com.winhxd.b2c.order.controller;

import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;

@RestController
public class OrderServiceController implements OrderServiceClient {

    @Override
    public ResponseResult<String> submitOrder(OrderCreateCondition orderCreateCondition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseResult<OrderInfo> getOrderVo(String orderNo) {
        // TODO Auto-generated method stub
        return null;
    }
}
