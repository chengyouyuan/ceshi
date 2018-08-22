package com.winhxd.b2c.pay.dao;

import java.util.List;

import com.winhxd.b2c.common.domain.pay.condition.PayStoreWalletCondition;
import com.winhxd.b2c.common.domain.pay.model.PayStoreWallet;

public interface PayStoreWalletMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStoreWallet record);

    int insertSelective(PayStoreWallet record);

    PayStoreWallet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStoreWallet record);

    int updateByPrimaryKey(PayStoreWallet record);
    
    List<PayStoreWallet> selectByStoreId(Long storeId);
    /**批量更新微信钱包的状态 置为0*/
	void updateBatchStatus();
	/**判断当前微信账户是否注册过*/
	List<PayStoreWallet> selectByCondtion(PayStoreWalletCondition condition);
}