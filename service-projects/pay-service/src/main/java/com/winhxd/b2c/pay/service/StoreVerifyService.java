package com.winhxd.b2c.pay.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import com.winhxd.b2c.pay.dao.AccountingDetailMapper;
import com.winhxd.b2c.pay.vo.StoreAndDateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 惠下单与门店结算
 */
@Service
public class StoreVerifyService {

    @Autowired
    private AccountingDetailMapper accountingDetailMapper;

    /**
     * 查询结算汇总
     *
     * @param verifyStatus       结算状态
     * @param storeId            门店ID
     * @param storeName          门店名称
     * @param toVerifyDateBefore 待结算日期之前(包括待结算日期)的费用将被结算
     * @param verifyDateStart    结算日期开始
     * @param verifyDateEnd      结算日期结束
     * @return
     */
    public Page<?> findVerifyList(Integer verifyStatus, Long storeId, String storeName,
                                  Date toVerifyDateBefore,
                                  Date verifyDateStart, Date verifyDateEnd,
                                  int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return accountingDetailMapper.selectVerifyList(verifyStatus, storeId, storeName,
                toVerifyDateBefore, verifyDateStart, verifyDateEnd);
    }

    /**
     * 查询费用明细
     *
     * @param verifyStatus              结算状态
     * @param storeId                   门店ID
     * @param storeName                 门店名称
     * @param thirdPartyVerifyStatus    第三方平台与惠下单结算状态
     * @param recordedDateStart         入账日期开始
     * @param recordedDateEnd           入账日期结算
     * @param thirdPartyVerifyDateStart 第三方平台结算日期开始
     * @param thirdPartyVerifyDateEnd   第三方平台结算日期结束
     * @param verifyDateStart           结算日期开始
     * @param verifyDateEnd             结算日期结束
     * @return
     */
    public Page<?> findAccountingDetailList(Integer verifyStatus, Long storeId, String storeName, Integer thirdPartyVerifyStatus,
                                            Date recordedDateStart, Date recordedDateEnd,
                                            Date thirdPartyVerifyDateStart, Date thirdPartyVerifyDateEnd,
                                            Date verifyDateStart, Date verifyDateEnd,
                                            int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return accountingDetailMapper.selectAccountingDetailList(
                verifyStatus, storeId, storeName, thirdPartyVerifyStatus,
                recordedDateStart, recordedDateEnd,
                thirdPartyVerifyDateStart, thirdPartyVerifyDateEnd,
                verifyDateStart, verifyDateEnd);
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
    public int verifyByStoreSummary(List<StoreAndDateVO> list, String verifyRemark, Long operatedBy, String operatedByName) {
        String uuid = UUID.randomUUID().toString();
        String verifyCode = Base64.getEncoder().encodeToString(uuid.getBytes());
        accountingDetailMapper.insertVerifyHistory(
                AccountingDetail.VerifyStatusEnum.VERIFIED.getCode(),
                verifyCode, verifyRemark, operatedBy, operatedByName);
        int updatedCount = 0;
        for (StoreAndDateVO vo : list) {
            Calendar c = Calendar.getInstance();
            c.setTime(vo.getDate());
            // 结算数据包括选择的日期，查询中使用小于下一日期实现，注：传入的日期时分秒需要为00:00:00,000
            c.add(Calendar.DAY_OF_YEAR, 1);
            int count = accountingDetailMapper
                    .updateAccountingDetailVerifyStatusBySummary(verifyCode, vo.getStoreId(), c.getTime());
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
        accountingDetailMapper.insertVerifyHistory(
                AccountingDetail.VerifyStatusEnum.VERIFIED.getCode(),
                verifyCode, verifyRemark, operatedBy, operatedByName);
        // 使用in批量更新
        int updatedCount = accountingDetailMapper.updateAccountingDetailVerifyStatusByDetailId(verifyCode, ids);
        return updatedCount;
    }
}
