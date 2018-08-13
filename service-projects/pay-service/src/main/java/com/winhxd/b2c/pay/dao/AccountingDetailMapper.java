package com.winhxd.b2c.pay.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
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
    int insertAccountingDetail(AccountingDetail accountingDetail);

    /**
     * 查询费用明细
     *
     * @param verifyStatus
     * @param storeId
     * @param storeName
     * @param thirdPartyVerifyStatus
     * @param recordedDateStart
     * @param recordedDateEnd
     * @param thirdPartyVerifyDateStart
     * @param thirdPartyVerifyDateEnd
     * @param verifyDateStart
     * @param verifyDateEnd
     * @return
     */
    Page<AccountingDetail> selectAccountingDetailList(@Param("verifyStatus") Integer verifyStatus,
                                                      @Param("storeId") Long storeId,
                                                      @Param("storeName") String storeName,
                                                      @Param("thirdPartyVerifyStatus") Integer thirdPartyVerifyStatus,
                                                      @Param("recordedDateStart") Date recordedDateStart,
                                                      @Param("recordedDateEnd") Date recordedDateEnd,
                                                      @Param("thirdPartyVerifyDateStart") Date thirdPartyVerifyDateStart,
                                                      @Param("thirdPartyVerifyDateEnd") Date thirdPartyVerifyDateEnd,
                                                      @Param("verifyDateStart") Date verifyDateStart,
                                                      @Param("verifyDateEnd") Date verifyDateEnd);

    /**
     * 查询结算汇总
     *
     * @param verifyStatus
     * @param storeId
     * @param storeName
     * @param toVerifyDateBefore
     * @param verifyDateStart
     * @param verifyDateEnd
     * @return
     */
    Page<?> selectVerifyList(@Param("verifyStatus") Integer verifyStatus,
                             @Param("storeId") Long storeId,
                             @Param("storeName") String storeName,
                             @Param("toVerifyDateBefore") Date toVerifyDateBefore,
                             @Param("verifyDateStart") Date verifyDateStart,
                             @Param("verifyDateEnd") Date verifyDateEnd);

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
     * @param verifyStatus
     * @param verifyCode
     * @param verifyRemark
     * @param operatedBy
     * @param operatedByName
     * @return
     */
    int insertVerifyHistory(@Param("verifyStatus") Integer verifyStatus,
                            @Param("verifyCode") String verifyCode,
                            @Param("verifyRemark") String verifyRemark,
                            @Param("operatedBy") Long operatedBy,
                            @Param("operatedByName") String operatedByName);

    /**
     * 按门店结算汇总更新费用明细状态
     *
     * @param verifyCode
     * @param storeId
     * @param toVerifyDateBefore
     * @return
     */
    int updateAccountingDetailVerifyStatusBySummary(@Param("verifyCode") String verifyCode,
                                                    @Param("storeId") Long storeId,
                                                    @Param("toVerifyDateBefore") Date toVerifyDateBefore);

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
