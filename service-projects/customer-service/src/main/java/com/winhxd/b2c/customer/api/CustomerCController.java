package com.winhxd.b2c.customer.api;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.vo.OrderVO;
import com.winhxd.b2c.common.feign.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerCController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("ss")
    public ResponseResult<OrderVO> getCustomerOrder(String orderNo) {
        ResponseResult<OrderVO> orderVo = orderService.getOrderVo(orderNo);
        return orderVo;
    }
}
