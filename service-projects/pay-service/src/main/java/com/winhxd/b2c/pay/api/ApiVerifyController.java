package com.winhxd.b2c.pay.api;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.OrderRecordAccountingCondition;
import com.winhxd.b2c.pay.service.VerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("结算接口外部接口")
@RequestMapping(value = "/api-pay/verify")
@RestController
public class ApiVerifyController {

    @Autowired
    private VerifyService verifyService;

    @ApiOperation("结算列表查询")
    @PostMapping("/691/v1/verifyList")
    public ResponseResult<ResponseResult> verifyList(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("结算-按汇总结算")
    @PostMapping("/692/v1/verifyBySummary")
    public ResponseResult<ResponseResult> verifyBySummary(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("费用明细列表查询")
    @PostMapping("/693/v1/accountingDetailList")
    public ResponseResult<ResponseResult> accountingDetailList(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("结算-按明细结算")
    @PostMapping("/694/v1/verifyByDetail")
    public ResponseResult<ResponseResult> verifyByDetail(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("门店提现申请列表查询")
    @PostMapping("/695/v1/storeWithdrawList")
    public ResponseResult<ResponseResult> storeWithdrawList(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("批准门店提现申请")
    @PostMapping("/696/v1/approveStoreWithdraw")
    public ResponseResult<ResponseResult> approveStoreWithdraw(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }
}
