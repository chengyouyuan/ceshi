package com.winhxd.b2c.store.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.StoreRegionCondition;
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
    int removeStoreRegion(Long id);

    /**
     * 保存测试门店区域
     * @author: wangbaokuo
     * @date: 2018/8/10 15:56
     * @param: conditions
     * @return:
     */
    int saveStoreRegion(StoreRegionCondition condition);
}
