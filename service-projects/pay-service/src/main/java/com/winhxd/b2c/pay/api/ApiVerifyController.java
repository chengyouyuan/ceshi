package com.winhxd.b2c.pay.api;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.pay.service.VerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("结算接口外部接口")
@RequestMapping(value = "/api-pay/verify")
@RestController
public class ApiVerifyController {

    @Autowired
    private VerifyService verifyService;

    @ApiOperation("结算列表查询")
    @GetMapping("/6xx/v1/verifyList")
    public ResponseResult<Object> verifyList() {
        return new ResponseResult<>();
    }

    @ApiOperation("结算-按汇总结算")
    @PostMapping("/6xx/v1/verifyBySummary")
    public ResponseResult<Object> verifyBySummary() {
        return new ResponseResult<>();
    }

    @ApiOperation("费用明细列表查询")
    @GetMapping("/6xx/v1/accountingDetailList")
    public ResponseResult<Object> accountingDetailList() {
        return new ResponseResult<>();
    }

    @ApiOperation("结算-按明细结算")
    @PostMapping("/6xx/v1/verifyByDetail")
    public ResponseResult<Object> verifyByDetail(@RequestParam("ids") String[] ids) {
        return new ResponseResult<>();
    }

    @ApiOperation("门店提现申请列表查询")
    @GetMapping("/6xx/v1/storeWithdrawList")
    public ResponseResult<Object> storeWithdrawList() {
        return new ResponseResult<>();
    }

    @ApiOperation("批准门店提现申请")
    @PatchMapping("/6xx/v1/approveStoreWithdraw")
    public ResponseResult<Object> approveStoreWithdraw(@RequestParam("ids") String[] ids) {
        return new ResponseResult<>();
    }
}
