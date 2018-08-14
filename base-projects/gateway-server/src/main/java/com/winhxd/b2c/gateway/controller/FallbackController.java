package com.winhxd.b2c.gateway.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @RequestMapping("/fallback")
    public ResponseResult fallback() {
        return new ResponseResult(BusinessCode.CODE_1001);
    }
}
