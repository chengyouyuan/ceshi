package com.winhxd.b2c.pay.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.pay.condition.VerifyDetailListCondition;
import com.winhxd.b2c.common.domain.pay.condition.VerifySummaryListCondition;
import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import com.winhxd.b2c.common.domain.pay.model.VerifyHistory;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifySummaryVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AccountingDetailMapper {

    /**
     * 插入费用明细数据
     *
     * @param accountingDetail
     * @return
     */
    AccountingDetail insertAccountingDetail(AccountingDetail accountingDetail);

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
     * 查询费用明细
     *
     * @param condition
     * @return
     */
    Page<VerifyDetailVO> selectAccountingDetailList(VerifyDetailListCondition condition);

    /**
     * 查询结算汇总
     *
     * @param condition
     * @return
     */
    Page<VerifySummaryVO> selectVerifyList(VerifySummaryListCondition condition);

    /**
     * 按订单更新费用明细更新订单费用状态为完成
     *
     * @param orderNo
     * @param recordedTime
     * @return
     */
    int updateAccountingDetailCompletedByComplete(@Param("orderNo") String orderNo, @Param("recordedTime") Date recordedTime);

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
    VerifyHistory insertVerifyHistory(VerifyHistory verifyHistory);

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
}
