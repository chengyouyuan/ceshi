package com.winhxd.b2c.admin.module.pay.controller;

import com.winhxd.b2c.admin.utils.ExcelUtils;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.feign.pay.VerifyServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "结算")
@RequestMapping("/pay/verify")
@RestController
public class VerifyController {

    @Autowired
    private VerifyServiceClient verifyServiceClient;

    @ApiOperation(value = "结算列表查询", notes = "按门店汇总")
    @PostMapping("/verifyList")
    public ResponseResult<?> verifyList(VerifySummaryListCondition condition) {
        return verifyServiceClient.verifyList(condition);
    }

    @ApiOperation(value = "账单结算", notes = "按汇总结算")
    @PostMapping("/verifyBySummary")
    public ResponseResult<?> verifyBySummary(VerifySummaryCondition condition) {
        return verifyServiceClient.verifyBySummary(condition);
    }

    @ApiOperation(value = "费用明细列表查询", notes = "按明细显示")
    @PostMapping("/accountingDetailList")
    public ResponseResult<?> accountingDetailList(VerifyDetailListCondition condition) {
        return verifyServiceClient.accountingDetailList(condition);
    }

    @ApiOperation(value = "费用明细导出Excel", notes = "按明细显示")
    @PostMapping(name = "/accountingDetailListExport", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> accountingDetailListExport(VerifyDetailListCondition condition) {
        condition.setIsQueryAll(true);
        ResponseResult<List<VerifyDetailVO>> responseResult = verifyServiceClient.accountingDetailListExport(condition);
        if (responseResult != null && responseResult.getCode() == 0) {
            List<VerifyDetailVO> list = responseResult.getData();
            return ExcelUtils.exp(list, "费用明细");
        }
        return null;
    }

    @ApiOperation(value = "费用结算", notes = "按明细结算")
    @PostMapping("/verifyByDetail")
    public ResponseResult<?> verifyByDetail(VerifyDetailCondition condition) {
        return verifyServiceClient.verifyByDetail(condition);
    }

    @ApiOperation(value = "费用明细暂缓", notes = "暂缓后，需要执行恢复才可以继续结算")
    @PostMapping("/accountingDetailPause")
    public ResponseResult<?> accountingDetailPause(VerifyDetailCondition condition) {
        return verifyServiceClient.accountingDetailPause(condition);
    }

    @ApiOperation(value = "费用明细暂缓恢复", notes = "重新加入到待结算账单中")
    @PostMapping("/accountingDetailRestore")
    public ResponseResult<?> accountingDetailRestore(VerifyDetailCondition condition) {
        return verifyServiceClient.accountingDetailRestore(condition);
    }

    @ApiOperation(value = "门店提现申请列表查询")
    @PostMapping("/storeWithdrawList")
    public ResponseResult<?> storeWithdrawList(PayWithdrawalsListCondition condition) {
        return verifyServiceClient.storeWithdrawalsList(condition);
    }

    @ApiOperation(value = "批准门店提现申请")
    @PostMapping("/approveStoreWithdraw")
    public ResponseResult<?> approveStoreWithdraw(ApproveStoreWithdrawalsCondition condition) {
        return verifyServiceClient.approveStoreWithdrawals(condition);
    }
}
