package com.winhxd.b2c.admin.module.pay.controller;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.VerifyDetailListCondition;
import com.winhxd.b2c.common.domain.pay.condition.VerifySummaryListCondition;
import com.winhxd.b2c.common.feign.pay.VerifyServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pay/verify")
@RestController
public class VerifyController {

    @Autowired
    private VerifyServiceClient verifyServiceClient;

    @GetMapping("/verifyList")
    public ResponseResult<?> verifyList(VerifySummaryListCondition condition) {
        return verifyServiceClient.verifyList(condition);
    }

    @GetMapping("/accountingDetailList")
    public ResponseResult<?> accountingDetailList(VerifyDetailListCondition condition) {
        return verifyServiceClient.accountingDetailList(condition);
    }
}
