package com.winhxd.b2c.pay.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.pay.condition.VerifyDetailListCondition;
import com.winhxd.b2c.common.domain.pay.condition.VerifySummaryCondition;
import com.winhxd.b2c.common.domain.pay.condition.VerifySummaryListCondition;
import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifySummaryVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.pay.dao.AccountingDetailMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class VerifyService {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private AccountingDetailMapper accountingDetailMapper;

    @Autowired
    private OrderServiceClient orderServiceClient;

    /**
     * 保存订单费用明细
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
                AccountingDetail realPay = new AccountingDetail();
                realPay.setOrderNo(orderNo);
                realPay.setDetailType(AccountingDetail.DetailTypeEnum.REAL_PAY.getCode());
                realPay.setDetailMoney(orderInfoDetailVO.getRealPaymentMoney());
                realPay.setStoreId(orderInfoDetailVO.getStoreId());
                realPay.setRecordedTime(orderInfoDetailVO.getFinishDateTime());
                accountingDetailMapper.insertAccountingDetail(realPay);
                count++;
                if (orderInfoDetailVO.getDiscountMoney() != null
                        && orderInfoDetailVO.getDiscountMoney().compareTo(BigDecimal.ZERO) != 0) {
                    AccountingDetail discount = new AccountingDetail();
                    discount.setOrderNo(orderNo);
                    discount.setDetailType(AccountingDetail.DetailTypeEnum.DISCOUNT.getCode());
                    discount.setDetailMoney(orderInfoDetailVO.getDiscountMoney());
                    discount.setStoreId(orderInfoDetailVO.getStoreId());
                    discount.setRecordedTime(orderInfoDetailVO.getFinishDateTime());
                    accountingDetailMapper.insertAccountingDetail(discount);
                    count++;
                }
            } else {
                throw new BusinessException(-1, "订单明细为NULL");
            }
        } else {
            throw new BusinessException(-1, String.format("订单服务-查询订单详情-返回失败-[%d]-%s",
                    responseResult.getCode(), responseResult.getMessage()));
        }
        log.info(String.format("保存[%d]笔订单费用明细-%s", count, orderNo));
        return count;
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
        accountingDetailMapper.insertVerifyHistory(
                AccountingDetail.VerifyStatusEnum.VERIFIED.getCode(),
                verifyCode, verifyRemark, operatedBy, operatedByName);
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
        accountingDetailMapper.insertVerifyHistory(
                AccountingDetail.VerifyStatusEnum.VERIFIED.getCode(),
                verifyCode, verifyRemark, operatedBy, operatedByName);
        // 使用in批量更新
        int updatedCount = accountingDetailMapper.updateAccountingDetailVerifyStatusByDetailId(verifyCode, ids);
        return updatedCount;
    }
}
