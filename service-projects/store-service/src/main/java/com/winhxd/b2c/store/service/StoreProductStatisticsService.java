
package com.winhxd.b2c.store.service;

import java.util.List;

import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductStatisticsCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductStatistics;

/**
 * 门店商品统计service
 * @ClassName: StoreProductStatisticsService 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月7日 上午9:55:52
 */
public interface StoreProductStatisticsService {

	/**
	 * 插入一条门店商品统计信息
	* @Title: saveStoreProductStatistics 
	* @Description: TODO 
	* @param record void
	* @author wuyuanbao
	* @date 2018年8月7日上午10:00:14
	 */
	void saveStoreProductStatistics(StoreProductStatistics record);
	
	/**
	 * 批量插入
	* @Title: bathSaveStoreProductStatistics 
	* @Description: TODO 
	* @param records void
	* @author wuyuanbao
	* @date 2018年8月9日下午5:42:13
	 */
	void bathSaveStoreProductStatistics(List<StoreProductStatistics> records);
	
	/**
	 * 更新一条门店商品统计信息
	* @Title: modifyStoreProductStatistics 
	* @Description: TODO 
	* @param record void
	* @author wuyuanbao
	* @date 2018年8月7日上午10:02:09
	 */
	void modifyStoreProductStatistics(StoreProductStatistics record);

}
