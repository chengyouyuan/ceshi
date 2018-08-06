package com.winhxd.b2c.store.service;

import java.util.List;

import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;

/**
 * 门段商品管理service
 * @ClassName: StoreProductManageService 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月4日 下午3:10:01
 */
public interface StoreProductManageService {
	/**
	 * 获取sku集合通过condition
	* @Title: selectSkusByConditon 
	* @Description: TODO 
	* @param condition
	* @return List<String>
	* @author wuyuanbao
	* @date 2018年8月6日上午9:28:05
	 */
	List<String> selectSkusByConditon(StoreProductManageCondition condition);

}
