package com.winhxd.b2c.admin.module.base.controller;

import com.winhxd.b2c.common.domain.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class UserController {


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseResult<Boolean> login() {
        return new ResponseResult<>(true);
    }
}
