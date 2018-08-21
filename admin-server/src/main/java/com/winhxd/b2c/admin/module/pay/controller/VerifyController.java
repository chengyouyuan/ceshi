package com.winhxd.b2c.admin.module.pay.controller;

import com.winhxd.b2c.admin.utils.ExcelUtils;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.vo.DoubleDate;
import com.winhxd.b2c.common.domain.pay.vo.DoubleDecimal;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.pay.VerifyServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Api(tags = "结算")
@RequestMapping("/pay/verify")
@Controller
public class VerifyController {

    @Autowired
    private VerifyServiceClient verifyServiceClient;

    @ApiOperation(value = "结算列表查询", notes = "按门店汇总")
    @RequestMapping("/verifyList")
    @ResponseBody
    public ResponseResult<?> verifyList(@RequestBody VerifySummaryListCondition condition) {
        DoubleDate recordedDate = condition.getRecordedDate();
        if (recordedDate != null) {
            condition.setRecordedDateStart(recordedDate.getStart());
            condition.setRecordedDateEnd(recordedDate.getEnd());
        }
        DoubleDate verifyDate = condition.getVerifyDate();
        if (verifyDate != null) {
            condition.setVerifyDateStart(verifyDate.getStart());
            condition.setVerifyDateEnd(verifyDate.getEnd());
        }
        DoubleDecimal realPayMoney = condition.getRealPayMoney();
        if (realPayMoney != null) {
            condition.setRealPayMoneyStart(realPayMoney.getStart());
            condition.setRealPayMoneyEnd(realPayMoney.getEnd());
        }
        DoubleDecimal realVerifyMoney = condition.getRealVerifyMoney();
        if (realVerifyMoney != null) {
            condition.setRealVerifyMoneyStart(realVerifyMoney.getStart());
            condition.setRealVerifyMoneyEnd(realVerifyMoney.getEnd());
        }
        return verifyServiceClient.verifyList(condition);
    }

    @ApiOperation(value = "账单结算", notes = "按汇总结算")
    @RequestMapping("/verifyBySummary")
    @ResponseBody
    public ResponseResult<?> verifyBySummary(@RequestBody List<VerifySummaryCondition.StoreAndDateVO> list) {
        VerifySummaryCondition condition = new VerifySummaryCondition();
        for (VerifySummaryCondition.StoreAndDateVO vo : list) {
            condition.getList().add(vo);
        }
        if (condition.getList().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        return verifyServiceClient.verifyBySummary(condition);
    }

    @ApiOperation(value = "费用明细列表查询", notes = "按明细显示")
    @RequestMapping("/accountingDetailList")
    @ResponseBody
    public ResponseResult<?> accountingDetailList(@RequestBody VerifyDetailListCondition condition) {
        DoubleDate recordedDate = condition.getRecordedDate();
        if (recordedDate != null) {
            condition.setRecordedDateStart(recordedDate.getStart());
            condition.setRecordedDateEnd(recordedDate.getEnd());
        }
        DoubleDate verifyDate = condition.getVerifyDate();
        if (verifyDate != null) {
            condition.setVerifyDateStart(verifyDate.getStart());
            condition.setVerifyDateEnd(verifyDate.getEnd());
        }
        return verifyServiceClient.accountingDetailList(condition);
    }

    @ApiOperation(value = "费用明细结算", notes = "按明细结算")
    @RequestMapping("/verifyByDetail")
    @ResponseBody
    public ResponseResult<?> verifyByDetail(@RequestBody List<Map<String, Object>> list) {
        VerifyDetailCondition condition = new VerifyDetailCondition();
        for (Map<String, Object> map : list) {
            Object id = map.get("id");
            if (id != null && NumberUtils.isCreatable(ObjectUtils.toString(id))) {
                condition.getIds().add(NumberUtils.createLong(ObjectUtils.toString(id)));
            }
        }
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        return verifyServiceClient.verifyByDetail(condition);
    }

    @ApiOperation(value = "费用明细暂缓", notes = "暂缓后，需要执行恢复才可以继续结算")
    @RequestMapping("/accountingDetailPause")
    @ResponseBody
    public ResponseResult<?> accountingDetailPause(@RequestBody List<Map<String, Object>> list) {
        VerifyDetailCondition condition = new VerifyDetailCondition();
        for (Map<String, Object> map : list) {
            Object id = map.get("id");
            if (id != null && NumberUtils.isCreatable(ObjectUtils.toString(id))) {
                condition.getIds().add(NumberUtils.createLong(ObjectUtils.toString(id)));
            }
        }
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        return verifyServiceClient.accountingDetailPause(condition);
    }

    @ApiOperation(value = "费用明细暂缓恢复", notes = "重新加入到待结算账单中")
    @RequestMapping("/accountingDetailRestore")
    @ResponseBody
    public ResponseResult<?> accountingDetailRestore(@RequestBody List<Map<String, Object>> list) {
        VerifyDetailCondition condition = new VerifyDetailCondition();
        for (Map<String, Object> map : list) {
            Object id = map.get("id");
            if (id != null && NumberUtils.isCreatable(ObjectUtils.toString(id))) {
                condition.getIds().add(NumberUtils.createLong(ObjectUtils.toString(id)));
            }
        }
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        return verifyServiceClient.accountingDetailRestore(condition);
    }

    @ApiOperation(value = "门店提现申请列表查询")
    @RequestMapping("/storeWithdrawList")
    @ResponseBody
    public ResponseResult<?> storeWithdrawList(@RequestBody PayWithdrawalsListCondition condition) {
        DoubleDate withdrawalsDate = condition.getWithdrawalsDate();
        if (withdrawalsDate != null) {
            condition.setWithdrawalsDateStart(withdrawalsDate.getStart());
            condition.setWithdrawalsDateEnd(withdrawalsDate.getEnd());
        }
        DoubleDecimal totalFee = condition.getTotalFee();
        if (totalFee != null) {
            condition.setTotalFeeStart(totalFee.getStart());
            condition.setTotalFeeEnd(totalFee.getEnd());
        }
        return verifyServiceClient.storeWithdrawalsList(condition);
    }

    @ApiOperation(value = "门店提现申请导出Excel")
    @RequestMapping("/storeWithdrawalsListExport")
    public ResponseEntity<byte[]> storeWithdrawalsListExport(@RequestBody PayWithdrawalsListCondition condition) {
        condition.setIsQueryAll(true);
        DoubleDate withdrawalsDate = condition.getWithdrawalsDate();
        if (withdrawalsDate != null) {
            condition.setWithdrawalsDateStart(withdrawalsDate.getStart());
            condition.setWithdrawalsDateEnd(withdrawalsDate.getEnd());
        }
        DoubleDecimal totalFee = condition.getTotalFee();
        if (totalFee != null) {
            condition.setTotalFeeStart(totalFee.getStart());
            condition.setTotalFeeEnd(totalFee.getEnd());
        }
        ResponseResult<List<PayWithdrawalsVO>> responseResult = verifyServiceClient.storeWithdrawalsListExport(condition);
        if (responseResult != null && responseResult.getCode() == 0) {
            List<PayWithdrawalsVO> list = responseResult.getData();
            return ExcelUtils.exp(list, "门店提现申请");
        }
        return null;
    }

    @ApiOperation(value = "批准门店提现申请")
    @RequestMapping("/approveStoreWithdraw")
    @ResponseBody
    public ResponseResult<?> approveStoreWithdraw(@RequestBody List<Map<String, Object>> list) {
        ApproveStoreWithdrawalsCondition condition = new ApproveStoreWithdrawalsCondition();
        for (Map<String, Object> map : list) {
            Object id = map.get("id");
            Object storeId = map.get("storeId");
            if (id != null && NumberUtils.isCreatable(ObjectUtils.toString(id))
                    && storeId != null && NumberUtils.isCreatable(ObjectUtils.toString(storeId))) {
                condition.getIds().add(NumberUtils.createLong(ObjectUtils.toString(id)));
                condition.getStoreIds().add(NumberUtils.createLong(ObjectUtils.toString(storeId)));
            }
        }
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        return verifyServiceClient.approveStoreWithdrawals(condition);
    }
}
