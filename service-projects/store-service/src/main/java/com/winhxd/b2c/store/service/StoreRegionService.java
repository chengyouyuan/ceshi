package com.winhxd.b2c.store.service;

import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.store.condition.StoreRegionCondition;
import com.winhxd.b2c.common.domain.store.model.StoreRegion;
import com.winhxd.b2c.common.domain.store.vo.StoreRegionVO;

/**
 * @author: wangbaokuo
 * @date: 2018/8/10 14:56
 */
public interface StoreRegionService {

    /**
     * 查询测试门店区域
     * @author: wangbaokuo
     * @date: 2018/8/10 15:56
     * @param: id
     * @return:
     */
    PagedList<StoreRegionVO> findStoreRegions(StoreRegionCondition condition);

    /**
     * 删除测试门店区域
     * @author: wangbaokuo
     * @date: 2018/8/10 15:56
     * @param: id
     * @return:
     */
    int removeStoreRegion(AdminUser adminUser,Long id);

    /**
     * 保存测试门店区域
     * @author: wangbaokuo
     * @date: 2018/8/10 15:56
     * @param: conditions
     * @return:
     */
    int saveStoreRegion(AdminUser adminUser,StoreRegionCondition condition);

    /**
     * 根据区域编码和级别查询是否是有效的测试区域
     * @param regionCode 区域编码
     * @return
     */
    StoreRegion getByRegionCode(String regionCode);

}
