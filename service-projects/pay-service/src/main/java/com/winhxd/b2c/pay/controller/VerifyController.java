package com.winhxd.b2c.pay.controller;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifyResultVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifySummaryVO;
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
    @PostMapping("/pay/6091/v1/verifyList")
    public ResponseResult<PagedList<VerifySummaryVO>> verifyList(VerifySummaryListCondition condition) {
        Page<VerifySummaryVO> page = verifyService.findVerifyList(condition);
        PagedList<VerifySummaryVO> pagedList = new PagedList<>();
        pagedList.setData(page.getResult());
        pagedList.setPageNo(page.getPageNum());
        pagedList.setPageSize(page.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return new ResponseResult<>(pagedList);
    }

    @ApiOperation("结算-按汇总结算")
    @PostMapping("/pay/6092/v1/verifyBySummary")
    public ResponseResult<?> verifyBySummary(VerifySummaryCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("费用明细列表查询")
    @PostMapping("/pay/6093/v1/accountingDetailList")
    public ResponseResult<PagedList<VerifyDetailVO>> accountingDetailList(VerifyDetailListCondition condition) {
        Page<VerifyDetailVO> page = verifyService.findAccountingDetailList(condition);
        PagedList<VerifyDetailVO> pagedList = new PagedList<>();
        pagedList.setData(page.getResult());
        pagedList.setPageNo(page.getPageNum());
        pagedList.setPageSize(page.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return new ResponseResult<>(pagedList);
    }

    @ApiOperation("结算-按明细结算")
    @PostMapping("/pay/6094/v1/verifyByDetail")
    public ResponseResult<VerifyResultVO> verifyByDetail(VerifyDetailCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("门店提现申请列表查询")
    @PostMapping("/pay/6095/v1/storeWithdrawList")
    public ResponseResult<VerifyResultVO> storeWithdrawList(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation("批准门店提现申请")
    @PostMapping("/pay/6096/v1/approveStoreWithdraw")
    public ResponseResult<VerifyResultVO> approveStoreWithdraw(OrderRecordAccountingCondition condition) {
        return new ResponseResult<>();
    }

    @ApiOperation(value = "订单费用记账", notes = "返回记账多少笔费用")
    @GetMapping("/pay/6097/v1/recordAccounting")
    public ResponseResult<Integer> recordAccounting(@RequestParam("orderNo") String orderNo) {
        int count = verifyService.saveAccountingDetailsByOrderNo(orderNo);
        return new ResponseResult(count);
    }
}
