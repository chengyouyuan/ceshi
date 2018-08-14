package com.winhxd.b2c.gateway.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    @RequestMapping("/fallback")
    public Mono<ResponseResult> fallback(Throwable throwable) {
        log.error("GatewayRouteFallback", throwable);
        return Mono.just(new ResponseResult(BusinessCode.CODE_1001));
    }
}
