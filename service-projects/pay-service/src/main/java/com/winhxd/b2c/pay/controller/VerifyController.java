package com.winhxd.b2c.pay.controller;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.pay.service.VerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("结算接口内部接口")
@RestController
public class VerifyController {

    @Autowired
    private VerifyService verifyService;

    @ApiOperation(value = "订单费用记账", notes = "返回该订单共有多少笔费用")
    @GetMapping("/pay/697/v1/recordAccounting")
    public ResponseResult<Integer> recordAccounting(@RequestParam("orderNo") String orderNo) {
        int count = verifyService.saveAccountingDetailsByOrderNo(orderNo);
        return new ResponseResult(count);
    }
}
