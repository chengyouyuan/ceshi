package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayStoreTransactionRecord;
import com.winhxd.b2c.common.domain.pay.vo.PayStoreTransactionRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayStoreTransactionRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStoreTransactionRecord record);

    int insertSelective(PayStoreTransactionRecord record);

    PayStoreTransactionRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStoreTransactionRecord record);

    int updateByPrimaryKey(PayStoreTransactionRecord record);

    List<PayStoreTransactionRecordVO> getPayStoreTransRecordByStoreId(@Param("storeId") Long storeId);
    
    List<PayStoreTransactionRecord> getPayStoreTransRecordByOrderNo(@Param("orderNo") String orderNo);

    int getTotalPayRecordToday(Long id);
    
}