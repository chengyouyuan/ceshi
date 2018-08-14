package com.winhxd.b2c.admin.module.order.controller;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.system.security.enums.MenuEnum;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.common.security.annotation.MenuAssign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@CheckPermission(PermissionEnum.SYSTEM_MANAGEMENT)
public class OrderController {
    
    @Autowired
    private OrderServiceClient orderServiceClient;
    
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.ORDER_MANAGEMENT_LIST)
    @MenuAssign(MenuEnum.ORDER_MANAGEMENT_LIST)
    public ResponseResult<PagedList<OrderInfoDetailVO>> list(@RequestBody OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition) {
        return orderServiceClient.listOrder4Management(infoQuery4ManagementCondition);
    }
}
