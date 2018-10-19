package com.winhxd.b2c.store.service;

import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreSubmitProdCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreSubmitProductCondition;
import com.winhxd.b2c.common.domain.store.model.StoreSubmitProduct;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreSubmitProdVO;
import com.winhxd.b2c.common.domain.store.vo.StoreSubmitProductVO;

/**
 * 提报商品service
 * @ClassName: StoreSubmitProductService 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月8日 下午6:12:35
 */
public interface StoreSubmitProductService {
	/**
	 * 添加StoreSubmitProduct实体
	* @Title: saveStoreSubmitProduct 
	* @Description: TODO 
	* @param storeId
	* @param storeSubmitProduct void
	* @author wuyuanbao
	* @date 2018年8月8日下午6:15:42
	 */
	void saveStoreSubmitProduct(Long storeId,StoreSubmitProduct storeSubmitProduct);

	/**
	 * 后台更新
	* @Title: modifyStoreSubmitProductByAdmin 
	* @Description: TODO 
	* @param adminUser
	* @param storeSubmitProduct void
	* @author wuyuanbao
	* @date 2018年8月8日下午6:26:02
	 */
	void modifyStoreSubmitProductByAdmin(AdminUser adminUser,StoreSubmitProduct storeSubmitProduct);
	/**
	 * 分页查询
	* @Title: findSimpelVOByCondition 
	* @Description: TODO 
	* @param condition
	* @return PagedList<StoreSubmitProductVO>
	* @author wuyuanbao
	* @date 2018年8月8日下午8:20:21
	 */
	PagedList<StoreSubmitProductVO> findSimpelVOByCondition(StoreSubmitProductCondition condition);
	/**
	 * 获取后台vo
	* @Title: findBackStageVOByCondition 
	* @Description: TODO 
	* @param condition
	* @return PagedList<BackStageStoreSubmitProdVO>
	* @author wuyuanbao
	* @date 2018年8月14日下午4:12:06
	 */
	PagedList<BackStageStoreSubmitProdVO> findBackStageVOByCondition(BackStageStoreSubmitProdCondition condition);
	/**
	 * 
	* @Title: findById 
	* @Description: TODO 
	* @param id
	* @return StoreSubmitProduct
	* @author wuyuanbao
	* @date 2018年8月23日下午3:02:24
	 */
	StoreSubmitProduct findById(Long id);
}
