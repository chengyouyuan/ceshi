package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.shopcar.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.shopcar.vo.ShopCarVO;

import java.util.List;

/**
 * @author: wangbaokuo
 * @date: 2018/8/2 20:19
 * @description:
 */
public interface ShopCarService {
    /**
     * 保存购物车
     * @author: wangbaokuo
     * @date: 2018/8/2 20:25
     * @param: CustomerShopCar
     * @return: int
     */
    int saveShopCar(ShopCarCondition shopCar);

    /**
     * 查询购物车
     * @author: wangbaokuo
     * @date: 2018/8/3 14:05
     * @param: condition
     * @return: List<ShopCarVO>
     */
    List<ShopCarVO> findShopCar(ShopCarCondition condition);
}
