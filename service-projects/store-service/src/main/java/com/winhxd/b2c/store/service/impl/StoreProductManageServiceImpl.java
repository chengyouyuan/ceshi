package com.winhxd.b2c.store.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.store.dao.StoreProductManageMapper;
import com.winhxd.b2c.store.dao.StoreProductStatisticsMapper;
import com.winhxd.b2c.store.service.StoreProductManageService;
/**
 * 门店商品管理Service实现类
 * @ClassName: StoreProductManageServiceImpl 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月4日 下午3:10:38
 */
@Service
public class StoreProductManageServiceImpl implements StoreProductManageService {
	@Autowired
	private StoreProductManageMapper storeProductManageMapper;
    @Autowired
    StoreProductStatisticsMapper storeProductStatisticsMapper;

	@Override
	public List<String> findSkusByConditon(StoreProductManageCondition condition) {
		
		return storeProductManageMapper.selectSkusByConditon(condition);
	}

	@Override
	public List<StoreProductManage> findProdBySkuCodes(Long storeId,String...skuCodes) {

		return storeProductManageMapper.selectPutawayProdBySkuCodes(storeId, skuCodes);
	}

	@Override
	public int countSkusByConditon(StoreProductManageCondition condition) {
		
		return storeProductManageMapper.countSkusByConditon(condition);
	}

	@Override
	@Transactional
	public void saveBatchStoreProductManage(Long storeId, List<StoreProductManage> storeProductList,StoreUser storeUser) {
		if(CollectionUtils.isNotEmpty(storeProductList)&&storeId!=null){
			
		}
		
	}
}
