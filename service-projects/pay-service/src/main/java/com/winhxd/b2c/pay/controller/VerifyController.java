package com.winhxd.b2c.pay.controller;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.OrderRecordAccountingCondition;
import com.winhxd.b2c.common.domain.pay.vo.VerifyResultVO;
import com.winhxd.b2c.pay.service.VerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("结算接口")
@RestController
public class VerifyController {

    @Autowired
    private VerifyService verifyService;

    @ApiOperation("结算列表查询")
    @PostMapping("/691/v1/verifyList")
    public ResponseResult<VerifyResultVO> verifyList(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("结算-按汇总结算")
    @PostMapping("/692/v1/verifyBySummary")
    public ResponseResult<VerifyResultVO> verifyBySummary(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("费用明细列表查询")
    @PostMapping("/693/v1/accountingDetailList")
    public ResponseResult<VerifyResultVO> accountingDetailList(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("结算-按明细结算")
    @PostMapping("/694/v1/verifyByDetail")
    public ResponseResult<VerifyResultVO> verifyByDetail(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("门店提现申请列表查询")
    @PostMapping("/695/v1/storeWithdrawList")
    public ResponseResult<VerifyResultVO> storeWithdrawList(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("批准门店提现申请")
    @PostMapping("/696/v1/approveStoreWithdraw")
    public ResponseResult<VerifyResultVO> approveStoreWithdraw(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation(value = "订单费用记账", notes = "返回该订单共有多少笔费用")
    @GetMapping("/pay/697/v1/recordAccounting")
    public ResponseResult<Integer> recordAccounting(@RequestParam("orderNo") String orderNo) {
        int count = verifyService.saveAccountingDetailsByOrderNo(orderNo);
        return new ResponseResult(count);
    }
}
