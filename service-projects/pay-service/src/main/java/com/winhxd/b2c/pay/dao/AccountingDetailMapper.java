package com.winhxd.b2c.pay.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.pay.condition.VerifyDetailListCondition;
import com.winhxd.b2c.common.domain.pay.condition.VerifySummaryListCondition;
import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import com.winhxd.b2c.common.domain.pay.model.VerifyHistory;
import com.winhxd.b2c.common.domain.pay.vo.OrderVerifyMoneyVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifySummaryVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AccountingDetailMapper {

    /**
     * 插入费用明细数据
     *
     * @param accountingDetail
     * @return
     */
    int insertAccountingDetail(AccountingDetail accountingDetail);

    /**
     * 单独插入订单手续费
     *
     * @param accountingDetail
     * @return
     */
    int insertAccountingDetailServiceFee(AccountingDetail accountingDetail);

    /**
     * 按指定ID查询的费用明细
     *
     * @param id
     * @return
     */
    AccountingDetail selectAccountingDetailById(@Param("id") Long id);

    /**
     * 按订单号查询的费用明细
     *
     * @param orderNo
     * @return
     */
    List<AccountingDetail> selectAccountingDetailListByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 按核销批次查询费用明细
     *
     * @param verifyCode
     * @return
     */
    List<AccountingDetail> selectAccountingDetailListByVerifyCode(@Param("verifyCode") String verifyCode);

    /**
     * 按核销批次查询订单应核销金额
     *
     * @param verifyCode
     * @return
     */
    List<OrderVerifyMoneyVO> selectOrderVerifyMoneyListByVerifyCode(@Param("verifyCode") String verifyCode);

    /**
     * 查询未标记支付平台已结算的费用订单号
     *
     * @return
     */
    List<String> selectThirdPartyNotVerifyOrderNoList();

    /**
     * 查询费用明细
     *
     * @param condition
     * @return
     */
    Page<VerifyDetailVO> selectAccountingDetailList(VerifyDetailListCondition condition);

    /**
     * 查询结算汇总，未结算
     *
     * @param condition
     * @return
     */
    Page<VerifySummaryVO> selectVerifyingList(VerifySummaryListCondition condition);

    /**
     * 查询结算汇总，已结算
     *
     * @param condition
     * @return
     */
    Page<VerifySummaryVO> selectVerifiedList(VerifySummaryListCondition condition);

    /**
     * 按订单更新费用明细更新订单费用状态为完成
     *
     * @param orderNo
     * @param recordedTime
     * @return
     */
    int updateAccountingDetailCompletedByComplete(@Param("orderNo") String orderNo, @Param("recordedTime") Date recordedTime);

    /**
     * 查询订单支付手续费
     *
     * @param orderNo
     * @return
     */
    BigDecimal selectAccountingDetailServiceFeeByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 按订单更新与第三方平台订单服务费
     *
     * @param orderNo
     * @param detailMoney
     * @return
     */
    int updateAccountingDetailServiceFeeByThirdParty(@Param("orderNo") String orderNo, @Param("detailMoney") BigDecimal detailMoney);

    /**
     * 按订单更新费用明细与第三方平台结算状态为已结算
     *
     * @param orderNo
     * @return
     */
    int updateAccountingDetailVerifiedByThirdParty(@Param("orderNo") String orderNo);

    /**
     * 插入结算历史
     *
     * @param verifyHistory
     * @return
     */
    int insertVerifyHistory(VerifyHistory verifyHistory);

    /**
     * 按门店结算汇总更新费用明细状态
     *
     * @param verifyCode
     * @param storeId
     * @param dateBefore
     * @return
     */
    int updateAccountingDetailVerifyStatusBySummary(@Param("verifyCode") String verifyCode,
                                                    @Param("storeId") Long storeId,
                                                    @Param("dateBefore") Date dateBefore);

    /**
     * 按费用明细ID更新费用明细状态
     *
     * @param verifyCode
     * @param ids
     * @return
     */
    int updateAccountingDetailVerifyStatusByDetailId(@Param("verifyCode") String verifyCode,
                                                     @Param("ids") List<Long> ids);

    /**
     * 按订单号删除费用明细，物理删除
     *
     * @param orderNo
     * @return
     */
    int deleteAccountingDetailByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 按订单号删除费用明细，逻辑删除
     *
     * @param orderNo
     * @return
     */
    int updateAccountingDetailLogicDeletedByOrderNo(@Param("orderNo") String orderNo);
}
