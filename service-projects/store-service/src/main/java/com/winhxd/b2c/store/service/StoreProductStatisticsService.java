package com.winhxd.b2c.store.service;

import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import org.springframework.stereotype.Service;

/**
 * @title: retail2c-backend
 * @description: 门店商品统计接口
 * @author: lvsen
 * @date: 2018/8/6 17:23
 * @version: 1.0
 */
@Service
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
}
