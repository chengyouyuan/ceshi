package com.winhxd.b2c.pay.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawals;
import com.winhxd.b2c.common.domain.pay.model.VerifyHistory;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifySummaryVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.pay.dao.AccountingDetailMapper;
import com.winhxd.b2c.pay.dao.PayWithdrawalsMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class VerifyService {

    private final Logger log = LogManager.getLogger(this.getClass());

    private static final BigDecimal FEE_RATE_OF_WX = BigDecimal.valueOf(0.006);

    @Autowired
    private AccountingDetailMapper accountingDetailMapper;

    @Autowired
    private PayWithdrawalsMapper payWithdrawalsMapper;

    @Autowired
    private OrderServiceClient orderServiceClient;

    /**
     * 订单支付成功，保存订单费用明细
     *
     * @param orderNo
     * @return
     */
    @Transactional
    public int saveAccountingDetailsByOrderNo(String orderNo) {
        int count = 0;
        // 调用订单服务，获取订单实付款
        ResponseResult<OrderInfoDetailVO4Management> responseResult = orderServiceClient.getOrderDetail4Management(orderNo);
        if (responseResult != null && responseResult.getCode() == 0) {
            OrderInfoDetailVO4Management orderInfoDetailVO4Management = responseResult.getData();
            OrderInfoDetailVO orderInfoDetailVO = orderInfoDetailVO4Management.getOrderInfoDetailVO();
            if (orderInfoDetailVO != null) {
                // 订单费用是否重复记账校验
                List<AccountingDetail> list = accountingDetailMapper.selectAccountingDetailListByOrderNo(orderNo);
                boolean isHasThirdPartyfee = false;
                boolean isHasRealPay = false;
                boolean isHasDiscount = false;
                for (AccountingDetail detail : list) {
                    if (detail.getDetailType().compareTo(AccountingDetail.DetailTypeEnum.FEE_OF_WX.getCode()) == 0) {
                        isHasThirdPartyfee = true;
                    }
                    if (detail.getDetailType().compareTo(AccountingDetail.DetailTypeEnum.REAL_PAY.getCode()) == 0) {
                        isHasRealPay = true;
                    }
                    if (detail.getDetailType().compareTo(AccountingDetail.DetailTypeEnum.DISCOUNT.getCode()) == 0) {
                        isHasDiscount = true;
                    }
                }
                if (isHasThirdPartyfee) {
                    // 插入微信支付手续费，按0.6%计算，如果有变更，另行调整
                    AccountingDetail thirdPartyfee = new AccountingDetail();
                    thirdPartyfee.setOrderNo(orderNo);
                    thirdPartyfee.setDetailType(AccountingDetail.DetailTypeEnum.FEE_OF_WX.getCode());
                    thirdPartyfee.setDetailMoney(FEE_RATE_OF_WX.multiply(orderInfoDetailVO.getRealPaymentMoney()));
                    thirdPartyfee.setStoreId(orderInfoDetailVO.getStoreId());
                    accountingDetailMapper.insertAccountingDetail(thirdPartyfee);
                    count++;
                }
                if (isHasRealPay) {
                    AccountingDetail realPay = new AccountingDetail();
                    realPay.setOrderNo(orderNo);
                    realPay.setDetailType(AccountingDetail.DetailTypeEnum.REAL_PAY.getCode());
                    realPay.setDetailMoney(orderInfoDetailVO.getRealPaymentMoney());
                    realPay.setStoreId(orderInfoDetailVO.getStoreId());
                    accountingDetailMapper.insertAccountingDetail(realPay);
                    count++;
                }
                if (isHasDiscount) {
                    if (orderInfoDetailVO.getDiscountMoney() != null
                            && orderInfoDetailVO.getDiscountMoney().compareTo(BigDecimal.ZERO) != 0) {
                        AccountingDetail discount = new AccountingDetail();
                        discount.setOrderNo(orderNo);
                        discount.setDetailType(AccountingDetail.DetailTypeEnum.DISCOUNT.getCode());
                        discount.setDetailMoney(orderInfoDetailVO.getDiscountMoney());
                        discount.setStoreId(orderInfoDetailVO.getStoreId());
                        accountingDetailMapper.insertAccountingDetail(discount);
                        count++;
                    }
                }
            } else {
                throw new BusinessException(-1, "订单明细为NULL");
            }
        } else {
            throw new BusinessException(-1, String.format("订单服务-查询订单详情-返回失败-[%d]-%s",
                    responseResult.getCode(), responseResult.getMessage()));
        }
        log.info(String.format("保存订单[%s]费用，共[%d]笔费用", orderNo, count));
        return count;
    }

    /**
     * 订单闭环，标记订单费用明细入账
     *
     * @param orderNo
     * @return
     */
    @Transactional
    public int completeAccounting(String orderNo) {
        int count;
        // 调用订单服务，获取订单闭环时间
        ResponseResult<OrderInfoDetailVO4Management> responseResult = orderServiceClient.getOrderDetail4Management(orderNo);
        if (responseResult != null && responseResult.getCode() == 0) {
            OrderInfoDetailVO4Management orderInfoDetailVO4Management = responseResult.getData();
            OrderInfoDetailVO orderInfoDetailVO = orderInfoDetailVO4Management.getOrderInfoDetailVO();
            if (orderInfoDetailVO != null) {
                count = accountingDetailMapper.updateAccountingDetailCompletedByComplete(
                        orderNo, orderInfoDetailVO.getFinishDateTime());
            } else {
                throw new BusinessException(-1, "订单明细为NULL");
            }
        } else {
            throw new BusinessException(-1, String.format("订单服务-查询订单详情-返回失败-[%d]-%s",
                    responseResult.getCode(), responseResult.getMessage()));
        }
        log.info(String.format("订单[%s]费用入账，共[%d]笔费用", orderNo, count));
        return count;
    }

    /**
     * 订单费用与支付平台结算
     *
     * @param condition
     * @return
     */
    @Transactional
    public int thirdPartyVerifyAccounting(ThirdPartyVerifyAccountingCondition condition) {
        return accountingDetailMapper.updateAccountingDetailVerifiedByThirdParty(condition.getOrderNo());
    }

    /**
     * 查询结算汇总
     *
     * @param condition
     * @return
     */
    public Page<VerifySummaryVO> findVerifyList(VerifySummaryListCondition condition) {
        PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        return accountingDetailMapper.selectVerifyList(condition);
    }

    /**
     * 查询费用明细
     *
     * @param condition
     * @return
     */
    public Page<VerifyDetailVO> findAccountingDetailList(VerifyDetailListCondition condition) {
        PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        return accountingDetailMapper.selectAccountingDetailList(condition);
    }

    /**
     * 按门店结算汇总结算
     *
     * @param list
     * @param verifyRemark
     * @param operatedBy
     * @param operatedByName
     */
    @Transactional
    public int verifyByStoreSummary(List<VerifySummaryCondition.StoreAndDateVO> list, String verifyRemark, Long operatedBy, String operatedByName) {
        String uuid = UUID.randomUUID().toString();
        String verifyCode = Base64.getEncoder().encodeToString(uuid.getBytes());
        VerifyHistory verifyHistory = new VerifyHistory();
        verifyHistory.setVerifyCode(verifyCode);
        verifyHistory.setVerifyStatus(AccountingDetail.VerifyStatusEnum.VERIFIED.getCode());
        verifyHistory.setVerifyRemark(verifyRemark);
        verifyHistory.setOperatedBy(operatedBy);
        verifyHistory.setOperatedByName(operatedByName);
        accountingDetailMapper.insertVerifyHistory(verifyHistory);
        int updatedCount = 0;
        for (VerifySummaryCondition.StoreAndDateVO vo : list) {
            int count = accountingDetailMapper.updateAccountingDetailVerifyStatusBySummary(
                    verifyCode, vo.getStoreId(), vo.getDate());
            updatedCount += count;
        }
        return updatedCount;
    }

    /**
     * 按费用明细结算
     *
     * @param ids
     * @param verifyRemark
     * @param operatedBy
     * @param operatedByName
     */
    @Transactional
    public int verifyByAccountingDetail(List<Long> ids, String verifyRemark, Long operatedBy, String operatedByName) {
        String uuid = UUID.randomUUID().toString();
        String verifyCode = Base64.getEncoder().encodeToString(uuid.getBytes());
        VerifyHistory verifyHistory = new VerifyHistory();
        verifyHistory.setVerifyCode(verifyCode);
        verifyHistory.setVerifyStatus(AccountingDetail.VerifyStatusEnum.VERIFIED.getCode());
        verifyHistory.setVerifyRemark(verifyRemark);
        verifyHistory.setOperatedBy(operatedBy);
        verifyHistory.setOperatedByName(operatedByName);
        accountingDetailMapper.insertVerifyHistory(verifyHistory);
        int updatedCount = accountingDetailMapper.updateAccountingDetailVerifyStatusByDetailId(verifyCode, ids);
        return updatedCount;
    }

    /**
     * 费用明细暂缓
     *
     * @param ids
     * @param verifyRemark
     * @param operatedBy
     * @param operatedByName
     * @return
     */
    @Transactional
    public int pauseByAccountingDetail(List<Long> ids, String verifyRemark, Long operatedBy, String operatedByName) {
        String uuid = UUID.randomUUID().toString();
        String verifyCode = Base64.getEncoder().encodeToString(uuid.getBytes());
        VerifyHistory verifyHistory = new VerifyHistory();
        verifyHistory.setVerifyCode(verifyCode);
        verifyHistory.setVerifyStatus(AccountingDetail.VerifyStatusEnum.PAUSED.getCode());
        verifyHistory.setVerifyRemark(verifyRemark);
        verifyHistory.setOperatedBy(operatedBy);
        verifyHistory.setOperatedByName(operatedByName);
        accountingDetailMapper.insertVerifyHistory(verifyHistory);
        int updatedCount = accountingDetailMapper.updateAccountingDetailVerifyStatusByDetailId(verifyCode, ids);
        return updatedCount;
    }

    /**
     * 费用明细暂缓恢复
     *
     * @param ids
     * @param verifyRemark
     * @param operatedBy
     * @param operatedByName
     * @return
     */
    @Transactional
    public int restoreByAccountingDetail(List<Long> ids, String verifyRemark, Long operatedBy, String operatedByName) {
        String uuid = UUID.randomUUID().toString();
        String verifyCode = Base64.getEncoder().encodeToString(uuid.getBytes());
        VerifyHistory verifyHistory = new VerifyHistory();
        verifyHistory.setVerifyCode(verifyCode);
        verifyHistory.setVerifyStatus(AccountingDetail.VerifyStatusEnum.NOT_VERIFIED.getCode());
        verifyHistory.setVerifyRemark(verifyRemark);
        verifyHistory.setOperatedBy(operatedBy);
        verifyHistory.setOperatedByName(operatedByName);
        accountingDetailMapper.insertVerifyHistory(verifyHistory);
        int updatedCount = accountingDetailMapper.updateAccountingDetailVerifyStatusByDetailId(verifyCode, ids);
        return updatedCount;
    }

    /**
     * 查询门店提现申请列表
     *
     * @param condition
     * @return
     */
    public Page<PayWithdrawalsVO> findPayWithdrawalsList(PayWithdrawalsListCondition condition) {
        PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        return payWithdrawalsMapper.selectPayWithdrawalsListByCondition(condition);
    }

    /**
     * 审核门店提现申请
     *
     * @param condition
     * @return
     */
    @Transactional
    public int approveWithdrawals(ApproveStoreWithdrawalsCondition condition) {
        int count = 0;
        Short auditStatus = condition.getAuditStatus();
        String auditDesc = condition.getAuditDesc();
        Long updatedBy = condition.getUpdatedBy();
        String updatedByName = condition.getUpdatedByName();
        for (Long id : condition.getIds()) {
            PayWithdrawals payWithdrawals = new PayWithdrawals();
            payWithdrawals.setId(id);
            payWithdrawals.setAuditStatus(auditStatus);
            payWithdrawals.setAuditDesc(auditDesc);
            payWithdrawals.setUpdated(new Date());
            payWithdrawals.setUpdatedBy(updatedBy);
            payWithdrawals.setUpdatedByName(updatedByName);
            payWithdrawalsMapper.updateByPrimaryKeySelective(payWithdrawals);
            count++;
        }
        return count;
    }
}
