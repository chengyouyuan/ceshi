package com.winhxd.b2c.pay.controller;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailExcelVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifySummaryVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.pay.service.VerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "结算")
@RestController
public class VerifyController {

    private static final Logger logger = LoggerFactory.getLogger(VerifyController.class);

    @Autowired
    private VerifyService verifyService;

    @ApiOperation(value = "订单费用记账", notes = "订单支付成功后，记录费用明细")
    @PostMapping("/pay/6081/v1/recordAccounting")
    public ResponseResult<Integer> recordAccounting(@RequestParam("orderNo") String orderNo) {
        logger.info("/pay/6081/v1/recordAccounting订单费用记账开始,orderNo为--{}", orderNo);
        int count = verifyService.saveAccountingDetailsByOrderNo(orderNo);
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "订单费用标记入账", notes = "订单闭环后，更新费用入账状态为已完成")
    @PostMapping("/pay/6082/v1/completeAccounting")
    public ResponseResult<Integer> completeAccounting(@RequestParam("orderNo") String orderNo) {
        logger.info("/pay/6082/v1/completeAccounting订单费用标记入账,orderNo为--{}", orderNo);
        int count = verifyService.completeAccounting(orderNo);
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "查询未标记支付平台已结算的费用订单号", notes = "用于标记与支付平台费用明细结算状态")
    @PostMapping("/pay/6083/v1/thirdPartyNotVerifyOrderNoList")
    public ResponseResult<List<String>> thirdPartyNotVerifyOrderNoList() {
        logger.info("/pay/6083/v1/thirdPartyNotVerifyOrderNoList查询未标记支付平台已结算的费用订单号");
        List<String> list = verifyService.thirdPartyNotVerifyOrderNoList();
        return new ResponseResult<>(list);
    }

    @ApiOperation(value = "订单费用与支付平台结算", notes = "支付平台结算后，更新费用明细与支付平台为结算完成")
    @PostMapping("/pay/6084/v1/thirdPartyVerifyAccounting")
    public ResponseResult<Integer> thirdPartyVerifyAccounting(@RequestBody ThirdPartyVerifyAccountingCondition condition) {
        logger.info("/pay/6084/v1/thirdPartyVerifyAccounting订单费用与支付平台结算，参数为--{}", JsonUtil.toJSONString(condition));
        int count = verifyService.thirdPartyVerifyAccounting(condition.getOrderNo());
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "结算列表查询", notes = "按门店汇总")
    @PostMapping("/pay/6091/v1/verifyList")
    public ResponseResult<PagedList<VerifySummaryVO>> verifyList(@RequestBody VerifySummaryListCondition condition) {
        logger.info("/pay/6091/v1/verifyList结算列表查询开始，参数为--{}", JsonUtil.toJSONString(condition));
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
        logger.info("/pay/6092/v1/verifyBySummary账单结算开始，参数为--{}", JsonUtil.toJSONString(condition));
        if (condition.getList().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        AdminUser adminUser = UserContext.getCurrentAdminUser();
        condition.setOperatedBy(adminUser.getId());
        condition.setOperatedByName(adminUser.getUsername());
        int count = verifyService.verifyByStoreSummary(
                condition.getList(), condition.getVerifyRemark(), condition.getOperatedBy(), condition.getOperatedByName());
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "费用明细列表查询", notes = "按明细显示")
    @PostMapping("/pay/6093/v1/accountingDetailList")
    public ResponseResult<PagedList<VerifyDetailVO>> accountingDetailList(@RequestBody VerifyDetailListCondition condition) {
        logger.info("/pay/6093/v1/accountingDetailList费用明细列表查询，参数为--{}", JsonUtil.toJSONString(condition));
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
        logger.info("/pay/6094/v1/verifyByDetail费用结算，参数为--{}", JsonUtil.toJSONString(condition));
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        AdminUser adminUser = UserContext.getCurrentAdminUser();
        condition.setOperatedBy(adminUser.getId());
        condition.setOperatedByName(adminUser.getUsername());
        int count = verifyService.verifyByAccountingDetail(
                condition.getIds(), condition.getVerifyRemark(), condition.getOperatedBy(), condition.getOperatedByName());
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "费用明细暂缓", notes = "暂缓后，需要执行恢复才可以继续结算")
    @PostMapping("/pay/6095/v1/accountingDetailPause")
    public ResponseResult<Integer> accountingDetailPause(@RequestBody VerifyDetailCondition condition) {
        logger.info("/pay/6095/v1/accountingDetailPause费用明细暂缓，参数为--{}", JsonUtil.toJSONString(condition));
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        AdminUser adminUser = UserContext.getCurrentAdminUser();
        condition.setOperatedBy(adminUser.getId());
        condition.setOperatedByName(adminUser.getUsername());
        int count = verifyService.pauseByAccountingDetail(
                condition.getIds(), condition.getVerifyRemark(), condition.getOperatedBy(), condition.getOperatedByName());
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "费用明细暂缓恢复", notes = "重新加入到待结算账单中")
    @PostMapping("/pay/6096/v1/accountingDetailRestore")
    public ResponseResult<Integer> accountingDetailRestore(@RequestBody VerifyDetailCondition condition) {
        logger.info("/pay/6096/v1/accountingDetailRestore费用明细暂缓恢复，参数为--{}", JsonUtil.toJSONString(condition));
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        AdminUser adminUser = UserContext.getCurrentAdminUser();
        condition.setOperatedBy(adminUser.getId());
        condition.setOperatedByName(adminUser.getUsername());
        int count = verifyService.restoreByAccountingDetail(
                condition.getIds(), condition.getVerifyRemark(), condition.getOperatedBy(), condition.getOperatedByName());
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "门店提现申请列表查询")
    @PostMapping("/pay/6097/v1/storeWithdrawalsList")
    public ResponseResult<PagedList<PayWithdrawalsVO>> storeWithdrawalsList(@RequestBody PayWithdrawalsListCondition condition) {
        logger.info("/pay/6097/v1/storeWithdrawalsList门店提现申请列表查询，参数为--{}", JsonUtil.toJSONString(condition));
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
        logger.info("/pay/6098/v1/approveStoreWithdrawals批准门店提现申请，参数为--{}", JsonUtil.toJSONString(condition));
        if (condition.getIds().size() == 0) {
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        AdminUser adminUser = UserContext.getCurrentAdminUser();
        condition.setUpdatedBy(adminUser.getId());
        condition.setUpdatedByName(adminUser.getUsername());
        int count = verifyService.approveWithdrawals(condition);
        return new ResponseResult<>(Integer.valueOf(count));
    }

    @ApiOperation(value = "门店提现申请导出查询")
    @PostMapping("/pay/6099/v1/storeWithdrawalsListExport")
    public ResponseResult<List<PayWithdrawalsVO>> storeWithdrawalsListExport(@RequestBody PayWithdrawalsListCondition condition) {
        logger.info("/pay/6099/v1/storeWithdrawalsListExport门店提现申请导出查询，参数为--{}", JsonUtil.toJSONString(condition));
        List<PayWithdrawalsVO> list = verifyService.findPayWithdrawalsList(condition);
        return new ResponseResult<>(list);
    }

    @ApiOperation(value = "结算列表查询", notes = "按门店汇总")
    @PostMapping("/pay/6085/v1/verifyListExport")
    public ResponseResult<List<VerifySummaryVO>> verifyListExport(@RequestBody VerifySummaryListCondition condition) {
        logger.info("/pay/6085/v1/verifyListExport结算列表查询开始，参数为--{}", JsonUtil.toJSONString(condition));
        List<VerifySummaryVO> verifyList = verifyService.findVerifyList(condition);
        return new ResponseResult<>(verifyList);
    }

    @ApiOperation(value = "费用明细导出查询")
    @PostMapping("/pay/6086/v1/accountingDetailListExport")
    public ResponseResult<List<VerifyDetailExcelVO>> accountingDetailListExport(@RequestBody VerifyDetailListCondition condition) {
        logger.info("/pay/6086/v1/accountingDetailListExport门店费用明细导出查询，参数为--{}", JsonUtil.toJSONString(condition));
        List<VerifyDetailVO> list = verifyService.findAccountingDetailList(condition);
        List<VerifyDetailExcelVO> excelList = list.stream().map(verifyDetailVO -> {
            VerifyDetailExcelVO verifyDetailExcelVO = new VerifyDetailExcelVO();
            BeanUtils.copyProperties(verifyDetailVO, verifyDetailExcelVO);
            return verifyDetailExcelVO;
        }).collect(Collectors.toList());
        return new ResponseResult<>(excelList);
    }

}
