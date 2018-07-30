package com.winhxd.b2c.admin.module.order.controller;

import com.winhxd.b2c.common.domain.system.security.enums.MenuEnum;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.common.security.annotation.MenuAssign;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
@CheckPermission(PermissionEnum.SYSTEM_MANAGEMENT)
public class OrderController {
    @RequestMapping(value = "/aaaa", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.ORDER_MANAGEMENT_LIST)
    @MenuAssign(MenuEnum.ORDER_MANAGEMENT_LIST)
    public Object ss() {
        return true;
    }

    @RequestMapping(value = "/bbb", method = RequestMethod.GET)
    @CheckPermission({PermissionEnum.ORDER_MANAGEMENT_EDIT, PermissionEnum.ORDER_MANAGEMENT})
    public Object s1() {
        return false;
    }
}
