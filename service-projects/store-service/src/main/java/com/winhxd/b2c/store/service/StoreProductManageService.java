package com.winhxd.b2c.store.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;

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
	/**
	 * 通过skuCodes获取对应门店的商品信息
	* @Title: findProdBySkuCodes 
	* @Description: TODO 
	* @param storeId
	* @param skuCodes
	* @return List<StoreProductManage>
	* @author wuyuanbao
	* @date 2018年8月6日下午4:05:43
	 */
	List<StoreProductManage> findProdBySkuCodes(Long storeId,String...skuCodes);
	
	/**
	 * 查询sku数量
	* @Title: countSkusByConditon 
	* @Description: TODO 
	* @param condition
	* @return int
	* @author wuyuanbao
	* @date 2018年8月6日下午8:16:45
	 */
	int countSkusByConditon(@Param("condition") StoreProductManageCondition condition);

}
