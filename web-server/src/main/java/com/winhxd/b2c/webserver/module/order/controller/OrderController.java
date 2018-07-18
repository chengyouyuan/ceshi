package com.winhxd.b2c.webserver.module.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @GetMapping("/test")
    public Object ss() {
        return true;
    }
}
