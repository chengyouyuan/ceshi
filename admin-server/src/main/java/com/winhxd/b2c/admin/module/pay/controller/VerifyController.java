package com.winhxd.b2c.admin.module.pay.controller;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.VerifyDetailCondition;
import com.winhxd.b2c.common.domain.pay.condition.VerifyDetailListCondition;
import com.winhxd.b2c.common.domain.pay.condition.VerifySummaryCondition;
import com.winhxd.b2c.common.domain.pay.condition.VerifySummaryListCondition;
import com.winhxd.b2c.common.feign.pay.VerifyServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("结算")
@RequestMapping("/pay/verify")
@RestController
public class VerifyController {

    @Autowired
    private VerifyServiceClient verifyServiceClient;

    @ApiOperation("结算汇总查询")
    @GetMapping("/verifyList")
    public ResponseResult<?> verifyList(VerifySummaryListCondition condition) {
        return verifyServiceClient.verifyList(condition);
    }

    @GetMapping("/verifyBySummary")
    public ResponseResult<?> verifyBySummary(VerifySummaryCondition condition) {
        return verifyServiceClient.verifyBySummary(condition);
    }

    @GetMapping("/accountingDetailList")
    public ResponseResult<?> accountingDetailList(VerifyDetailListCondition condition) {
        return verifyServiceClient.accountingDetailList(condition);
    }

    @GetMapping("/verifyByDetail")
    public ResponseResult<?> verifyByDetail(VerifyDetailCondition condition) {
        return verifyServiceClient.verifyByDetail(condition);
    }
}
