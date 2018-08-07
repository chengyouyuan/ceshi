package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.order.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.order.vo.ShopCarVO;

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
    void saveShopCar(ShopCarCondition shopCar);

    /**
     * 查询购物车
     * @author: wangbaokuo
     * @date: 2018/8/3 14:05
     * @param: condition
     * @return: ShopCarVO
     */
    ShopCarVO findShopCar(ShopCarCondition condition);

    /**
     * 删除购物车
     * @author: wangbaokuo
     * @date: 2018/8/3 16:20
     * @param: condition
     * @return: int
     */
    int removeShopCar(ShopCarCondition condition);

    /**
     *  预订单
     * @author: wangbaokuo
     * @date: 2018/8/6 16:41
     * @param: condition
     * @return: void
     */
    void readyOrder(ShopCarCondition condition);
}
