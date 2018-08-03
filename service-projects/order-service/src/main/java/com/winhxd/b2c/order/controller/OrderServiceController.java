package com.winhxd.b2c.order.controller;

import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.order.service.OrderService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderServiceController implements OrderService {
    /**
     * 订单提交接口
     *
     * @param orderCreateCondition
     * @return 订单号
     * @author wangbin
     * @date 2018年8月2日 下午5:51:46
     */
    @Override
    public String submitOrder(OrderCreateCondition orderCreateCondition) {
        return null;
    }
}
