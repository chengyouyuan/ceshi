package com.winhxd.b2c.order.controller;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.vo.OrderVO;
import com.winhxd.b2c.common.feign.order.OrderService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderServiceController implements OrderService {
    @Override
    public ResponseResult<OrderVO> getOrderVo(@RequestParam("orderNo") String orderNo) {
        OrderVO vo = new OrderVO();
        vo.setOrderNo(orderNo);
        vo.setStoreId(10L);
        return new ResponseResult<>(vo);
    }
}
