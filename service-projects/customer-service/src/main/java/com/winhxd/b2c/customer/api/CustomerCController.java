package com.winhxd.b2c.customer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.vo.OrderVO;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;

@RestController
public class CustomerCController {
    @Autowired
    private OrderServiceClient orderService;

    @RequestMapping("ss")
    public ResponseResult<OrderVO> getCustomerOrder(String orderNo) {
        ResponseResult<OrderVO> orderVo = orderService.getOrderVo(orderNo);
        return orderVo;
    }
}
