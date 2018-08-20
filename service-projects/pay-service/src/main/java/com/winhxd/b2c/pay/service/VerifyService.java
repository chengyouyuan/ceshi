package com.winhxd.b2c.pay.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.constant.WXCalculation;
import com.winhxd.b2c.common.domain.pay.enums.StoreBankRollOpearateEnums;
import com.winhxd.b2c.common.domain.pay.enums.StoreTransactionStatusEnum;
import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import com.winhxd.b2c.common.domain.pay.model.PayStoreTransactionRecord;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawals;
import com.winhxd.b2c.common.domain.pay.model.VerifyHistory;
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
    private PayStoreCashService payStoreCashService;

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
     * 订单闭环事件
     *
     * @param orderNo
     * @param orderInfo
     */
    @EventMessageListener(value = EventTypeHandler.ACCOUNTING_DETAIL_RECORDED_HANDLER, concurrency = "3-6")
    public void orderFinishHandler(String orderNo, OrderInfo orderInfo) {
        completeAccounting(orderInfo.getOrderNo());
        //计算门店资金
        //手续费
        BigDecimal cmmsAmt=orderInfo.getRealPaymentMoney().multiply(WXCalculation.FEE_RATE_OF_WX).setScale(WXCalculation.DECIMAL_NUMBER, WXCalculation.DECIMAL_CALCULATION);
        //门店应得金额（订单总额-手续费）
        BigDecimal money=orderInfo.getOrderTotalMoney().subtract(cmmsAmt);
        UpdateStoreBankRollCondition condition=new UpdateStoreBankRollCondition();
        condition.setOrderNo(orderNo);
        condition.setStoreId(orderInfo.getStoreId());
        condition.setMoney(money);
        condition.setType(StoreBankRollOpearateEnums.ORDER_FINISH.getCode());
        payService.updateStoreBankroll(condition);
        //添加交易记录
        PayStoreTransactionRecord payStoreTransactionRecord=new PayStoreTransactionRecord();
        payStoreTransactionRecord.setStoreId(orderInfo.getStoreId());
        payStoreTransactionRecord.setOrderNo(orderNo);
        payStoreTransactionRecord.setType(StoreTransactionStatusEnum.ORDER_ENTRY.getStatusCode());
        payStoreTransactionRecord.setMoney(money);
        payStoreTransactionRecord.setRate(WXCalculation.FEE_RATE_OF_WX);
        payStoreTransactionRecord.setCmmsAmt(cmmsAmt);
        payStoreCashService.savePayStoreTransactionRecord(payStoreTransactionRecord);
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
                if (isHasThirdPartyfee) {
                    // 插入微信支付手续费，按0.6%计算，如果有变更，另行调整
                    AccountingDetail thirdPartyfee = new AccountingDetail();
                    thirdPartyfee.setOrderNo(orderNo);
                    thirdPartyfee.setDetailType(AccountingDetail.DetailTypeEnum.FEE_OF_WX.getCode());
                    thirdPartyfee.setDetailMoney(WXCalculation.FEE_RATE_OF_WX.multiply(orderInfoDetailVO.getRealPaymentMoney()));
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
        Set<Long> storeSet = new HashSet<>();
        Page<VerifySummaryVO> page = accountingDetailMapper.selectVerifyList(condition);
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
        return page;
    }

    /**
     * 查询费用明细
     *
     * @param condition
     * @return
     */
    public Page<VerifyDetailVO> findAccountingDetailList(VerifyDetailListCondition condition) {
        PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        Set<Long> storeSet = new HashSet<>();
        Page<VerifyDetailVO> page = accountingDetailMapper.selectAccountingDetailList(condition);
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
                    verifyCode, vo.getStoreId(), vo.getDate());
            updatedCount += count;
        }
        // 门店资金变动
        UpdateStoreBankRollCondition rollCondition = new UpdateStoreBankRollCondition();
        payService.updateStoreBankroll(rollCondition);
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
        // 门店资金变动
        UpdateStoreBankRollCondition rollCondition = new UpdateStoreBankRollCondition();
        payService.updateStoreBankroll(rollCondition);
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
        Set<Long> storeSet = new HashSet<>();
        Page<PayWithdrawalsVO> page = payWithdrawalsMapper.selectPayWithdrawalsListByCondition(condition);
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
        for (Long id : condition.getIds()) {
            PayWithdrawals payWithdrawals = new PayWithdrawals();
            payWithdrawals.setId(id);
            payWithdrawals.setUpdated(new Date());
            payWithdrawals.setUpdatedBy(updatedBy);
            payWithdrawals.setUpdatedByName(updatedByName);
            // 更新审核状态
            payWithdrawals.setAuditStatus(auditStatus);
            payWithdrawals.setAuditDesc(auditDesc);
            payWithdrawalsMapper.updateByPrimaryKeySelective(payWithdrawals);
            // 门店资金变动
            UpdateStoreBankRollCondition rollCondition = new UpdateStoreBankRollCondition();
            payService.updateStoreBankroll(rollCondition);
            // 更新门店提现状态
            payWithdrawals.setAuditStatus(null);
            payWithdrawals.setAuditDesc(null);
            payWithdrawals.setCallbackStatus(null);
            payWithdrawals.setCallbackReason(null);
            payWithdrawalsMapper.updateByPrimaryKeySelective(payWithdrawals);
            count++;
        }
        return count;
    }
    public static void main(String[] args) {
		System.out.println(BigDecimal.valueOf(9.8767).setScale(2, BigDecimal.ROUND_HALF_UP));
	}
}
