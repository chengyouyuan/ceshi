
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
     * 功能描述: 修改门店售卖数量
     * @param condition
     * @param condition
     * @return int
     * @auther lvsen
     * @date 2018/8/6 17:34
     */
    int modifyQuantitySoldOutByStoreIdAndProdId(StoreProductManageCondition condition);
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
	 * 更新一条门店商品统计信息
	* @Title: modifyStoreProductStatistics 
	* @Description: TODO 
	* @param record void
	* @author wuyuanbao
	* @date 2018年8月7日上午10:02:09
	 */
	void modifyStoreProductStatistics(StoreProductStatistics record);
	
	/**
	 * 查询门店商品统计信息
	* @Title: findStoreProductStatisticsByCondition 
	* @Description: TODO 
	* @param condition
	* @return List<StoreProductStatistics>
	* @author wuyuanbao
	* @date 2018年8月7日上午10:05:05
	 */
	List<StoreProductStatistics> findStoreProductStatisticsByCondition(StoreProductStatisticsCondition condition);
}
