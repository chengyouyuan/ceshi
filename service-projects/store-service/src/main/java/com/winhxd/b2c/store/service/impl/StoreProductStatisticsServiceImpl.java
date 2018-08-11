
package com.winhxd.b2c.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductStatisticsCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductStatistics;
import com.winhxd.b2c.store.dao.StoreProductStatisticsMapper;
import com.winhxd.b2c.store.service.StoreProductStatisticsService;
/**
 * 门店商品统计service实现类
 * @ClassName: StoreProductStatisticsServiceImpl 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月7日 上午9:59:14
 */
@Service
public class StoreProductStatisticsServiceImpl implements StoreProductStatisticsService{
    @Autowired
    StoreProductStatisticsMapper storeProductStatisticsMapper;


	@Override
	public void saveStoreProductStatistics(StoreProductStatistics record) {
		storeProductStatisticsMapper.insertSelective(record);
		
	}

	@Override
	public void modifyStoreProductStatistics(StoreProductStatistics record) {
		storeProductStatisticsMapper.updateByPrimaryKeySelective(record);
		
	}

	@Override
	@Transactional
	public void bathSaveStoreProductStatistics(List<StoreProductStatistics> records) {
		if (records != null) {
			for (StoreProductStatistics record : records) {
				this.saveStoreProductStatistics(record);
			}
		}
	}

}
