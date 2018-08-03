package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.shopcar.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.shopcar.vo.ShopCarVO;

import java.util.List;

/**
 * @auther: wangbaokuo
 * @date: 2018/8/2 20:19
 * @description:
 */
public interface ShopCarService {
    /**
     * @auther: wangbaokuo
     * @date: 2018/8/2 20:25
     * @deprecated: 保存购物车
     * @param: CustomerShopCar
     * @return: int
     */
    int saveShopCar(ShopCarCondition shopCar);

    /**
     * @auther: wangbaokuo
     * @date: 2018/8/3 14:05
     * @deprecated: 查询购物车
     * @param: condition
     * @return: List<ShopCarVO>
     */
    List<ShopCarVO> findShopCar(ShopCarCondition condition);
}
