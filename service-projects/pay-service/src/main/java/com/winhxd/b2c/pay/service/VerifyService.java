package com.winhxd.b2c.pay.service;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifySummaryVO;

import java.util.*;

public interface VerifyService {

    /**
     * 订单支付成功，保存订单费用明细
     *
     * @param orderNo
     * @return
     */
    int saveAccountingDetailsByOrderNo(String orderNo);

    /**
     * 订单闭环，标记订单费用明细入账
     *
     * @param orderNo
     * @return
     */
    int completeAccounting(String orderNo);

    /**
     * 订单取消，删除订单费用明细(逻辑删除)
     *
     * @param orderNo
     * @return
     */
    int removeAccountingDetailByOrderNo(String orderNo);

    /**
     * 查询未标记支付平台已结算的费用订单号，不包含当天的订单数据
     *
     * @return
     */
    List<String> thirdPartyNotVerifyOrderNoList();

    /**
     * 订单费用与支付平台结算
     *
     * @param orderNo
     * @return
     */
    int thirdPartyVerifyAccounting(String orderNo);

    /**
     * 查询结算汇总
     *
     * @param condition
     * @return
     */
    Page<VerifySummaryVO> findVerifyList(VerifySummaryListCondition condition);

    /**
     * 查询费用明细
     *
     * @param condition
     * @return
     */
    Page<VerifyDetailVO> findAccountingDetailList(VerifyDetailListCondition condition);

    /**
     * 按门店结算汇总结算
     *
     * @param list
     * @param verifyRemark
     * @param operatedBy
     * @param operatedByName
     */
    int verifyByStoreSummary(List<VerifySummaryCondition.StoreAndDateVO> list, String verifyRemark, Long operatedBy, String operatedByName);

    /**
     * 按费用明细结算
     *
     * @param ids
     * @param verifyRemark
     * @param operatedBy
     * @param operatedByName
     */
    int verifyByAccountingDetail(List<Long> ids, String verifyRemark, Long operatedBy, String operatedByName);

    /**
     * 费用明细暂缓
     *
     * @param ids
     * @param verifyRemark
     * @param operatedBy
     * @param operatedByName
     * @return
     */
    int pauseByAccountingDetail(List<Long> ids, String verifyRemark, Long operatedBy, String operatedByName);

    /**
     * 费用明细暂缓恢复
     *
     * @param ids
     * @param verifyRemark
     * @param operatedBy
     * @param operatedByName
     * @return
     */
    int restoreByAccountingDetail(List<Long> ids, String verifyRemark, Long operatedBy, String operatedByName);

    /**
     * 查询门店提现申请列表
     *
     * @param condition
     * @return
     */
    Page<PayWithdrawalsVO> findPayWithdrawalsList(PayWithdrawalsListCondition condition);

    /**
     * 审核门店提现申请
     *
     * @param condition
     * @return
     */
    int approveWithdrawals(ApproveStoreWithdrawalsCondition condition);
}
