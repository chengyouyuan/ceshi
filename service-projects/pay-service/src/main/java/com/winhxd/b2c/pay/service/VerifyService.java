package com.winhxd.b2c.pay.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.constant.WXCalculation;
import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import com.winhxd.b2c.common.domain.pay.model.PayStatement;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawals;
import com.winhxd.b2c.common.domain.pay.model.VerifyHistory;
import com.winhxd.b2c.common.domain.pay.vo.OrderVerifyMoneyVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifySummaryVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.mq.event.EventMessageListener;
import com.winhxd.b2c.common.mq.event.EventTypeHandler;
import com.winhxd.b2c.pay.dao.AccountingDetailMapper;
import com.winhxd.b2c.pay.dao.PayWithdrawalsMapper;
import com.winhxd.b2c.pay.weixin.dao.PayStatementMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VerifyService {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private AccountingDetailMapper accountingDetailMapper;

    @Autowired
    private PayWithdrawalsMapper payWithdrawalsMapper;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private StoreServiceClient storeServiceClient;

    @Autowired
    private PayService payService;

    @Autowired
    private PayStatementMapper payStatementMapper;

    /**
     * 订单支付成功事件
     *
     * @param orderNo
     * @param orderInfo
     */
    @EventMessageListener(value = EventTypeHandler.ACCOUNTING_DETAIL_SAVE_HANDLER, concurrency = "3-6")
    public void orderPaySuccessHandler(String orderNo, OrderInfo orderInfo) {
        saveAccountingDetailsByOrderNo(orderInfo.getOrderNo());
    }

    /**
     * 订单闭环，费用记账
     *
     * @param orderNo
     * @param orderInfo
     */
    @EventMessageListener(value = EventTypeHandler.ACCOUNTING_DETAIL_RECORDED_HANDLER, concurrency = "3-6")
    public void orderFinishHandler(String orderNo, OrderInfo orderInfo) {
        completeAccounting(orderInfo.getOrderNo());
    }

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
                if (!isHasThirdPartyfee) {
                    // 插入微信支付手续费，按0.6%计算，如果有变更，另行调整
                    AccountingDetail thirdPartyfee = new AccountingDetail();
                    thirdPartyfee.setOrderNo(orderNo);
                    thirdPartyfee.setDetailType(AccountingDetail.DetailTypeEnum.FEE_OF_WX.getCode());
                    BigDecimal fee = WXCalculation.FEE_RATE_OF_WX
                            .multiply(orderInfoDetailVO.getRealPaymentMoney())
                            .setScale(WXCalculation.DECIMAL_NUMBER, WXCalculation.DECIMAL_CALCULATION);
                    thirdPartyfee.setDetailMoney(fee.multiply(BigDecimal.valueOf(-1)));
                    thirdPartyfee.setStoreId(orderInfoDetailVO.getStoreId());
                    if (BigDecimal.ZERO.compareTo(fee) == 0) {
                        accountingDetailMapper.insertAccountingDetail(thirdPartyfee);
                        count++;
                    }
                }
                if (!isHasRealPay) {
                    AccountingDetail realPay = new AccountingDetail();
                    realPay.setOrderNo(orderNo);
                    realPay.setDetailType(AccountingDetail.DetailTypeEnum.REAL_PAY.getCode());
                    realPay.setDetailMoney(orderInfoDetailVO.getRealPaymentMoney());
                    realPay.setStoreId(orderInfoDetailVO.getStoreId());
                    accountingDetailMapper.insertAccountingDetail(realPay);
                    count++;
                }
                if (!isHasDiscount) {
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
                log.info(String.format("保存订单[%s]费用明细，共[%d]笔费用", orderNo, count));
            } else {
                throw new BusinessException(-1, "订单明细为NULL");
            }
        } else {
            throw new BusinessException(-1, String.format("订单服务-查询订单详情-返回失败-[%d]-%s",
                    responseResult.getCode(), responseResult.getMessage()));
        }
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
        String timeFormat = "yyyy-MM-dd HH:mm:ss,SSS";
        int count;
        // 调用订单服务，获取订单闭环时间
        ResponseResult<OrderInfoDetailVO4Management> responseResult = orderServiceClient.getOrderDetail4Management(orderNo);
        if (responseResult != null && responseResult.getCode() == 0) {
            OrderInfoDetailVO4Management orderInfoDetailVO4Management = responseResult.getData();
            OrderInfoDetailVO orderInfoDetailVO = orderInfoDetailVO4Management.getOrderInfoDetailVO();
            if (orderInfoDetailVO != null) {
                if (orderInfoDetailVO.getFinishDateTime() == null) {
                    throw new BusinessException(-1, "订单闭环时间为NULL");
                }
                count = accountingDetailMapper.updateAccountingDetailCompletedByComplete(
                        orderNo, orderInfoDetailVO.getFinishDateTime());
                log.info(String.format("订单[%s]费用明细入账，入账时间[%s]，共[%d]笔费用",
                        orderNo, new SimpleDateFormat(timeFormat).format(orderInfoDetailVO.getFinishDateTime()), count));
            } else {
                throw new BusinessException(-1, "订单明细为NULL");
            }
        } else {
            throw new BusinessException(-1, String.format("订单服务-查询订单详情-返回失败-[%d]-%s",
                    responseResult.getCode(), responseResult.getMessage()));
        }
        return count;
    }

    /**
     * 查询未标记支付平台已结算的费用订单号，不包含当天的订单数据
     *
     * @return
     */
    public List<String> thirdPartyNotVerifyOrderNoList() {
        return accountingDetailMapper.selectThirdPartyNotVerifyOrderNoList();
    }

    /**
     * 订单费用与支付平台结算
     *
     * @param orderNo
     * @return
     */
    @Transactional
    public int thirdPartyVerifyAccounting(String orderNo) {
        int updateCount = 0;
        ResponseResult<OrderInfoDetailVO4Management> responseResult = orderServiceClient.getOrderDetail4Management(orderNo);
        if (responseResult != null && responseResult.getCode() == 0) {
            OrderInfoDetailVO4Management orderInfoDetailVO4Management = responseResult.getData();
            if (orderInfoDetailVO4Management != null) {
                OrderInfoDetailVO orderInfoDetailVO = orderInfoDetailVO4Management.getOrderInfoDetailVO();
                if (orderInfoDetailVO != null) {
                    PayStatement payStatement = payStatementMapper.selectByOutOrderNo(orderInfoDetailVO.getPaymentSerialNum());
                    if (payStatement == null) {
                        log.warn("没有查询到订单[{}]与支付平台对账信息", orderNo);
                        return 0;
                    }
                    // 订单手续费
                    BigDecimal serviceFee = payStatement.getFee();
                    if (BigDecimal.ZERO.compareTo(serviceFee) != 0) {
                        // 惠下单平台在订单支付成功时计算出的手续费，因为只有在出对账单时才知道手续费是多少
                        BigDecimal computeServiceFee = accountingDetailMapper.selectAccountingDetailServiceFeeByOrderNo(orderNo);
                        if (computeServiceFee == null) {
                            AccountingDetail thirdPartyfee = new AccountingDetail();
                            thirdPartyfee.setOrderNo(orderNo);
                            thirdPartyfee.setDetailType(AccountingDetail.DetailTypeEnum.FEE_OF_WX.getCode());
                            thirdPartyfee.setDetailMoney(serviceFee.multiply(BigDecimal.valueOf(-1)));
                            thirdPartyfee.setStoreId(orderInfoDetailVO.getStoreId());
                            if (orderInfoDetailVO.getOrderStatus().compareTo(OrderStatusEnum.FINISHED.getStatusCode()) == 1) {
                                thirdPartyfee.setOrderCompleteStatus(1);
                                thirdPartyfee.setRecordedTime(orderInfoDetailVO.getFinishDateTime());
                            } else {
                                thirdPartyfee.setOrderCompleteStatus(0);
                            }
                            accountingDetailMapper.insertAccountingDetailServiceFee(thirdPartyfee);
                        }
                        accountingDetailMapper.updateAccountingDetailServiceFeeByThirdParty(
                                orderNo, serviceFee.multiply(BigDecimal.valueOf(-1)));
                    }
                    updateCount = accountingDetailMapper.updateAccountingDetailVerifiedByThirdParty(orderNo);
                    if (updateCount > 0) {
                        log.info("订单[{}]与支付平台结算，手续费[{}]，共更新[{}]条费用明细", orderNo, serviceFee, updateCount);
                    }
                }
            }
        }
        return updateCount;
    }

    /**
     * 查询结算汇总
     *
     * @param condition
     * @return
     */
    public Page<VerifySummaryVO> findVerifyList(VerifySummaryListCondition condition) {
        PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        Page<VerifySummaryVO> page = accountingDetailMapper.selectVerifyList(condition);
        if (page.getTotal() > 0) {
            Set<Long> storeSet = new HashSet<>();
            for (VerifySummaryVO vo : page.getResult()) {
                if (!storeSet.contains(vo.getStoreId())) {
                    storeSet.add(vo.getStoreId());
                }
            }
            Map<Long, String> storeMap = new HashMap<>();
            ResponseResult<List<StoreUserInfoVO>> responseResult = storeServiceClient.findStoreUserInfoList(storeSet);
            if (responseResult != null && responseResult.getCode() == 0) {
                if (responseResult.getData() != null) {
                    for (StoreUserInfoVO vo : responseResult.getData()) {
                        storeMap.put(vo.getId(), vo.getStoreName());
                    }
                }
            }
            for (VerifySummaryVO vo : page.getResult()) {
                vo.setStoreName(storeMap.get(vo.getStoreId()));
            }
        }
        return page;
    }

    /**
     * 查询费用明细
     *
     * @param condition
     * @return
     */
    public Page<VerifyDetailVO> findAccountingDetailList(VerifyDetailListCondition condition) {
        if (!condition.getIsQueryAll()) {
            PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        }
        Page<VerifyDetailVO> page = accountingDetailMapper.selectAccountingDetailList(condition);
        if (page.getTotal() > 0) {
            Set<Long> storeSet = new HashSet<>();
            for (VerifyDetailVO vo : page.getResult()) {
                if (!storeSet.contains(vo.getStoreId())) {
                    storeSet.add(vo.getStoreId());
                }
            }
            Map<Long, String> storeMap = new HashMap<>();
            ResponseResult<List<StoreUserInfoVO>> responseResult = storeServiceClient.findStoreUserInfoList(storeSet);
            if (responseResult != null && responseResult.getCode() == 0) {
                if (responseResult.getData() != null) {
                    for (StoreUserInfoVO vo : responseResult.getData()) {
                        storeMap.put(vo.getId(), vo.getStoreName());
                    }
                }
            }
            for (VerifyDetailVO vo : page.getResult()) {
                vo.setStoreName(storeMap.get(vo.getStoreId()));
            }
        }
        return page;
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
                    verifyCode, vo.getStoreId(), vo.getLastRecordedTime());
            updatedCount += count;
        }
        notifyStoreMoneyVerify(verifyCode);
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
        notifyStoreMoneyVerify(verifyCode);
        return updatedCount;
    }

    /**
     * 门店资金结算，按核销批次结算
     *
     * @param verifyCode
     * @return
     */
    @Transactional
    private int notifyStoreMoneyVerify(String verifyCode) {
        int updatedCount = 0;
        List<OrderVerifyMoneyVO> orderVerifyMoneyVOList = accountingDetailMapper.selectOrderVerifyMoneyListByVerifyCode(verifyCode);
        for (OrderVerifyMoneyVO vo : orderVerifyMoneyVOList) {
            UpdateStoreBankRollCondition rollCondition = new UpdateStoreBankRollCondition();
            // 2.结算审核
            rollCondition.setType(2);
            rollCondition.setOrderNo(vo.getOrderNo());
            rollCondition.setStoreId(vo.getStoreId());
            // 货款
            if (BigDecimal.ZERO.compareTo(vo.getPaymentMoney()) != 0) {
                rollCondition.setMoneyType((short) 1);
                rollCondition.setMoney(vo.getPaymentMoney());
                payService.updateStoreBankroll(rollCondition);
            }
            // 促销费
            if (BigDecimal.ZERO.compareTo(vo.getDiscountMoney()) != 0) {
                rollCondition.setMoneyType((short) 2);
                rollCondition.setMoney(vo.getDiscountMoney().abs());
                payService.updateStoreBankroll(rollCondition);
            }
            updatedCount++;
        }
        log.info("门店资金结算-核销批次[{}}，共结算[{}]笔订单", verifyCode, updatedCount);
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
        if (!condition.getIsQueryAll()) {
            PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        }
        Page<PayWithdrawalsVO> page = payWithdrawalsMapper.selectPayWithdrawalsListByCondition(condition);
        if (page.getTotal() > 0) {
            Set<Long> storeSet = new HashSet<>();
            for (PayWithdrawalsVO vo : page.getResult()) {
                if (!storeSet.contains(vo.getStoreId())) {
                    storeSet.add(vo.getStoreId());
                }
            }
            Map<Long, String> storeMap = new HashMap<>();
            ResponseResult<List<StoreUserInfoVO>> responseResult = storeServiceClient.findStoreUserInfoList(storeSet);
            if (responseResult != null && responseResult.getCode() == 0) {
                if (responseResult.getData() != null) {
                    for (StoreUserInfoVO vo : responseResult.getData()) {
                        storeMap.put(vo.getId(), vo.getStoreName());
                    }
                }
            }
            for (PayWithdrawalsVO vo : page.getResult()) {
                vo.setStoreName(storeMap.get(vo.getStoreId()));
            }
        }
        return page;
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
        for (int i = 0; i < condition.getIds().size(); i++) {
            PayWithdrawals payWithdrawals = new PayWithdrawals();
            Long id = condition.getIds().get(i);
            payWithdrawals.setId(id);
            payWithdrawals.setUpdated(new Date());
            payWithdrawals.setUpdatedBy(updatedBy);
            payWithdrawals.setUpdatedByName(updatedByName);
            // 更新审核状态
            payWithdrawals.setAuditStatus(auditStatus);
            payWithdrawals.setAuditDesc(auditDesc);
            payWithdrawalsMapper.updateByPrimaryKeySelective(payWithdrawals);
            // 门店提现
            PayWithdrawalsCondition toWxBankCondition = new PayWithdrawalsCondition();
            toWxBankCondition.setWithdrawalsId(id);
            toWxBankCondition.setOperaterID(String.valueOf(condition.getUpdatedBy()));
            payService.transfersPatrent(toWxBankCondition);
            count++;
        }
        return count;
    }
}
