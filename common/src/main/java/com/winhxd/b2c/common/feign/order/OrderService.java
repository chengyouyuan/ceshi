package com.winhxd.b2c.common.feign.order;

import com.winhxd.b2c.common.constant.ServiceName;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = ServiceName.ORDER_SERVICE)
public interface OrderService {
}
