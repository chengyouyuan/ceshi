package com.winhxd.b2c.store.service.impl;

import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.store.dao.StoreProductStatisticsMapper;
import com.winhxd.b2c.store.service.StoreProductStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @title: retail2c-backend
 * @description: 门店商品统计实现类
 * @author: lvsen
 * @date: 2018/8/6 17:23
 * @version: 1.0
 */
@Service
public class StoreProductStatisticsServiceImpl implements StoreProductStatisticsService {

    @Autowired
    StoreProductStatisticsMapper storeProductStatisticsMapper;

    @Override
    public int modifyQuantitySoldOutByStoreIdAndProdId(StoreProductManageCondition condition) {
        return storeProductStatisticsMapper.updateQuantitySoldOutByStoreIdAndProdId(condition);
    }
}
