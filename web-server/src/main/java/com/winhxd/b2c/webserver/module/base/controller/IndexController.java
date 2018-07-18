package com.winhxd.b2c.webserver.module.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "base/index";
    }
}
