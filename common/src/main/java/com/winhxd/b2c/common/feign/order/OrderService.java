package com.winhxd.b2c.common.feign.order;

import com.winhxd.b2c.common.constant.ServiceNameConstant;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = ServiceNameConstant.ORDER_SERVICE)
public interface OrderService {
}
