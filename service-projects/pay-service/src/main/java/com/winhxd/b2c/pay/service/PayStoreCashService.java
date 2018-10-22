package com.winhxd.b2c.pay.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreCashCondition;
import com.winhxd.b2c.common.domain.pay.model.PayStoreTransactionRecord;
import com.winhxd.b2c.common.domain.pay.vo.PayStoreTransactionRecordVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankrollVO;

/**
 * @Author wl
 * @Date 2018/8/14 17:03
 * @Description
 **/
public interface PayStoreCashService {
    /**
     *
     *@Deccription 资金提现首页
     *@Params condition
     *@Return ResponseResult<StoreBankrollVO>
     *@User  wl
     *@Date   2018/8/22 9:30
     */
    StoreBankrollVO getStoreBankrollByStoreId(PayStoreCashCondition condition, Long storeId);
    /**
     *
     *@Deccription 门店交易记录收支明细
     *@Params  condition
     *@Return  ResponseResult<PagedList<PayStoreTransactionRecordVO>>
     *@User  wl
     *@Date   2018/8/22 9:31
     */
    PagedList<PayStoreTransactionRecordVO> getPayStoreTransRecordByStoreId(PayStoreCashCondition condition, Long storeId);

    /**
     *
     *@Deccription 门店提现记录
     *@Params  condition
     *@Return  ResponseResult<PagedList<PayWithdrawalsVO>>
     *@User  wl
     *@Date   2018/8/22 9:31
     */
    PagedList<PayWithdrawalsVO> getPayWithdrawalsByStoreId(PayStoreCashCondition condition, Long storeId);
    
    
    /**
     * @author liuhanning
     * @date  2018年8月18日 上午10:52:02
     * @Description 保存交易记录
     * @param payStoreTransactionRecord
     */
    void savePayStoreTransactionRecord(PayStoreTransactionRecord payStoreTransactionRecord);
    
    
}
