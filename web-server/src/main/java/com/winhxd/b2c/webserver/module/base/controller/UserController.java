package com.winhxd.b2c.webserver.module.base.controller;

import com.winhxd.b2c.common.domain.RestResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class UserController {


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RestResult<Boolean> login() {
        return new RestResult<>(true);
    }
}
