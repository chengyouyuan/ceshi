package com.winhxd.b2c.store.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.ProdOperateInfoCondition;
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
	List<String> findSkusByConditon(StoreProductManageCondition condition);
	/**
	 * 通过skuCodes获取对应门店的上架商品信息
	* @Title: findPutawayProdBySkuCodes 
	* @Description: TODO 
	* @param storeId
	* @param skuCodes
	* @return List<StoreProductManage>
	* @author wuyuanbao
	* @date 2018年8月6日下午4:05:43
	 */
	List<StoreProductManage> findPutawayProdBySkuCodes(Long storeId,String...skuCodes);
	
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
	
	/**
	 * 商品上架操作（支持批量）
	* @Title: batchPutawayStoreProductManage 
	* @Description: TODO 
	* @param storeId
	* @param putawayInfo
	* @param prodSkuInfo void
	* @author wuyuanbao
	* @date 2018年8月8日下午3:26:52
	 */
	void batchPutawayStoreProductManage(Long storeId,Map<String,ProdOperateInfoCondition> putawayInfo,Map<String,ProductSkuVO> prodSkuInfo);

}
