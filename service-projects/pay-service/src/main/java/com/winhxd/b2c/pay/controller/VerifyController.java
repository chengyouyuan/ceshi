package com.winhxd.b2c.pay.controller;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifySummaryVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.service.VerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "结算")
@RestController
public class VerifyController {

    @Autowired
    private VerifyService verifyService;

    @ApiOperation(value = "订单费用记账", notes = "订单支付成功后，记录费用明细")
    @PostMapping("/pay/6081/v1/recordAccounting")
    public ResponseResult<Integer> recordAccounting(@RequestParam("orderNo") String orderNo) {
        int count = verifyService.saveAccountingDetailsByOrderNo(orderNo);
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "订单费用标记入账", notes = "订单闭环后，更新费用入账状态为已完成")
    @PostMapping("/pay/6082/v1/completeAccounting")
    public ResponseResult<Integer> completeAccounting(@RequestParam("orderNo") String orderNo) {
        int count = verifyService.completeAccounting(orderNo);
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "查询未标记支付平台已结算的费用订单号", notes = "用于标记与支付平台费用明细结算状态")
    @PostMapping("/pay/6083/v1/thirdPartyNotVerifyOrderNoList")
    public ResponseResult<List<String>> thirdPartyNotVerifyOrderNoList() {
        List<String> list = verifyService.thirdPartyNotVerifyOrderNoList();
        return new ResponseResult<>(list);
    }

    @ApiOperation(value = "订单费用与支付平台结算", notes = "支付平台结算后，更新费用明细与支付平台为结算完成")
    @PostMapping("/pay/6084/v1/thirdPartyVerifyAccounting")
    public ResponseResult<Integer> thirdPartyVerifyAccounting(@RequestBody ThirdPartyVerifyAccountingCondition condition) {
        int count = verifyService.thirdPartyVerifyAccounting(condition.getOrderNo());
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "结算列表查询", notes = "按门店汇总")
    @PostMapping("/pay/6091/v1/verifyList")
    public ResponseResult<PagedList<VerifySummaryVO>> verifyList(@RequestBody VerifySummaryListCondition condition) {
        Page<VerifySummaryVO> page = verifyService.findVerifyList(condition);
        PagedList<VerifySummaryVO> pagedList = new PagedList<>();
        pagedList.setData(page.getResult());
        pagedList.setPageNo(page.getPageNum());
        pagedList.setPageSize(page.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return new ResponseResult<>(pagedList);
    }

    @ApiOperation(value = "账单结算", notes = "按汇总结算")
    @PostMapping("/pay/6092/v1/verifyBySummary")
    public ResponseResult<Integer> verifyBySummary(@RequestBody VerifySummaryCondition condition) {
        if (condition.getList().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        int count = verifyService.verifyByStoreSummary(
                condition.getList(), condition.getVerifyRemark(), condition.getOperatedBy(), condition.getOperatedByName());
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "费用明细列表查询", notes = "按明细显示")
    @PostMapping("/pay/6093/v1/accountingDetailList")
    public ResponseResult<PagedList<VerifyDetailVO>> accountingDetailList(@RequestBody VerifyDetailListCondition condition) {
        Page<VerifyDetailVO> page = verifyService.findAccountingDetailList(condition);
        PagedList<VerifyDetailVO> pagedList = new PagedList<>();
        pagedList.setData(page.getResult());
        pagedList.setPageNo(page.getPageNum());
        pagedList.setPageSize(page.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return new ResponseResult<>(pagedList);
    }

    @ApiOperation(value = "费用结算", notes = "按明细结算")
    @PostMapping("/pay/6094/v1/verifyByDetail")
    public ResponseResult<Integer> verifyByDetail(@RequestBody VerifyDetailCondition condition) {
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        int count = verifyService.verifyByAccountingDetail(
                condition.getIds(), condition.getVerifyRemark(), condition.getOperatedBy(), condition.getOperatedByName());
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "费用明细暂缓", notes = "暂缓后，需要执行恢复才可以继续结算")
    @PostMapping("/pay/6095/v1/accountingDetailPause")
    public ResponseResult<Integer> accountingDetailPause(@RequestBody VerifyDetailCondition condition) {
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        int count = verifyService.pauseByAccountingDetail(
                condition.getIds(), condition.getVerifyRemark(), condition.getOperatedBy(), condition.getOperatedByName());
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "费用明细暂缓恢复", notes = "重新加入到待结算账单中")
    @PostMapping("/pay/6096/v1/accountingDetailRestore")
    public ResponseResult<Integer> accountingDetailRestore(@RequestBody VerifyDetailCondition condition) {
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        int count = verifyService.restoreByAccountingDetail(
                condition.getIds(), condition.getVerifyRemark(), condition.getOperatedBy(), condition.getOperatedByName());
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "门店提现申请列表查询")
    @PostMapping("/pay/6097/v1/storeWithdrawalsList")
    public ResponseResult<PagedList<PayWithdrawalsVO>> storeWithdrawalsList(@RequestBody PayWithdrawalsListCondition condition) {
        Page<PayWithdrawalsVO> page = verifyService.findPayWithdrawalsList(condition);
        PagedList<PayWithdrawalsVO> pagedList = new PagedList<>();
        pagedList.setData(page.getResult());
        pagedList.setPageNo(page.getPageNum());
        pagedList.setPageSize(page.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return new ResponseResult<>(pagedList);
    }

    @ApiOperation(value = "批准门店提现申请")
    @PostMapping("/pay/6098/v1/approveStoreWithdrawals")
    public ResponseResult<Integer> approveStoreWithdrawals(@RequestBody ApproveStoreWithdrawalsCondition condition) {
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        int count = verifyService.approveWithdrawals(condition);
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "门店提现申请导出查询")
    @PostMapping("/pay/6099/v1/storeWithdrawalsListExport")
    public ResponseResult<List<PayWithdrawalsVO>> storeWithdrawalsListExport(@RequestBody PayWithdrawalsListCondition condition) {
        List<PayWithdrawalsVO> list = verifyService.findPayWithdrawalsList(condition);
        return new ResponseResult<>(list);
    }
}
