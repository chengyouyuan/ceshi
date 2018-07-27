package com.winhxd.b2c.webserver.module.order.controller;

import com.winhxd.b2c.webserver.common.security.annotation.CheckPermission;
import com.winhxd.b2c.webserver.common.security.Menu;
import com.winhxd.b2c.webserver.common.security.annotation.MenuAssign;
import com.winhxd.b2c.webserver.common.security.Permission;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
@CheckPermission(Permission.SYSTEM_MANAGEMENT)
public class OrderController {
    @RequestMapping(value = "/aaaa", method = RequestMethod.POST)
    @CheckPermission(Permission.ORDER_MANAGEMENT_LIST)
    @MenuAssign(Menu.ORDER_MANAGEMENT_LIST)
    public Object ss() {
        return true;
    }

    @RequestMapping(value = "/bbb", method = RequestMethod.GET)
    @CheckPermission({Permission.ORDER_MANAGEMENT_EDIT, Permission.ORDER_MANAGEMENT})
    public Object s1() {
        return false;
    }
}
