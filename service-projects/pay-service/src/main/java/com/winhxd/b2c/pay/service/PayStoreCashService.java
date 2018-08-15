package com.winhxd.b2c.pay.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreCashCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayStoreTransactionRecordVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankrollVO;

/**
 * @Author wl
 * @Date 2018/8/14 17:03
 * @Description
 **/
public interface PayStoreCashService {
    ResponseResult<StoreBankrollVO> getStoreBankrollByStoreId(PayStoreCashCondition condition);
    ResponseResult<PagedList<PayStoreTransactionRecordVO>> getPayStoreTransRecordByStoreId(PayStoreCashCondition condition);
    ResponseResult<PagedList<PayWithdrawalsVO>> getPayWithdrawalsByStoreId(PayStoreCashCondition condition);
}