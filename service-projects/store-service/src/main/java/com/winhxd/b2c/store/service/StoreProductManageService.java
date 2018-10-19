package com.winhxd.b2c.store.service;

import java.util.List;
import java.util.Map;

import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreProdCondition;
import com.winhxd.b2c.common.domain.store.condition.ProdOperateInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreProdVO;
import com.winhxd.b2c.common.domain.store.vo.StoreProdSimpleVO;

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
	List<String> countSkusByConditon(StoreProductManageCondition condition);
	
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
	
	/**
	 * 删除门店商品信息（逻辑删除）支持批量
	* @Title: removeStoreProductManage 
	* @Description: TODO 
	* @param storeId
	* @param skuCodes void
	* @author wuyuanbao
	* @date 2018年8月8日下午5:05:47
	 */
	
	void removeStoreProductManage(Long storeId,String...skuCodes);
	
	/**
	 * 下架门店商品信息支持批量
	* @Title: unPutawayStoreProductManage 
	* @Description: TODO 
	* @param storeId
	* @param skuCodes void
	* @author wuyuanbao
	* @date 2018年8月8日下午5:08:09
	 */
	void unPutawayStoreProductManage(Long storeId,String...skuCodes);
	
	/**
	 * 更新门店商品信息（价格，是否推荐）
	* @Title: modifyStoreProductManage 
	* @Description: TODO 
	* @param storeId
	* @param prodOperateInfo void
	* @author wuyuanbao
	* @date 2018年8月8日下午5:42:52
	 */
	void modifyStoreProductManage(Long storeId,ProdOperateInfoCondition prodOperateInfo);
	
	/**
	 * 分页查询
	* @Title: findSimpelVOByCondition 
	* @Description: TODO 
	* @param condition
	* @return PagedList<StoreProdSimpleVO>
	* @author wuyuanbao
	* @date 2018年8月8日下午9:12:27
	 */
	PagedList<StoreProdSimpleVO> findSimpelVOByCondition(StoreProductManageCondition condition);


	/**
	 * 根据参数搜索商品集合
	 * @param storeProductManageCondition
	 * @return
	 */
	List<StoreProductManage> findProductBySelective(StoreProductManageCondition storeProductManageCondition);
	
	/**
	 * 后台分页查询
	* @Title: findStoreProdManageList 
	* @Description: TODO 
	* @param condition
	* @return PagedList<BackStageStoreProdVO>
	* @author wuyuanbao
	* @date 2018年8月13日下午1:45:33
	 */
	PagedList<BackStageStoreProdVO> findStoreProdManageList(BackStageStoreProdCondition condition);
	
	/**
	 * 后台更新信息
	* @Title: modifyStoreProdManageByBackStage 
	* @Description: TODO 
	* @param condition void
	* @author wuyuanbao
	* @date 2018年8月13日下午1:47:50
	 */
	void modifyStoreProdManageByBackStage(AdminUser adminUser,BackStageStoreProdCondition condition);
}
