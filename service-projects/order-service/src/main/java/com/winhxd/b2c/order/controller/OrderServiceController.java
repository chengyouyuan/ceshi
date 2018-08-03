package com.winhxd.b2c.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderVO;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;

@RestController
public class OrderServiceController implements OrderServiceClient {
    @Autowired
    private Cache cache;

    @Override
    public ResponseResult<OrderVO> getOrderVo(@RequestParam("orderNo") String orderNo) {
        cache.setex("11111", 120, "111111");
        OrderVO vo = new OrderVO();
        vo.setOrderNo(orderNo);
        vo.setStoreId(10L);
        return new ResponseResult<>(vo);
    }

    @Override
    public ResponseResult<OrderVO> submitOrder(OrderCreateCondition orderCreateCondition) {
        // TODO Auto-generated method stub
        return null;
    }
}
