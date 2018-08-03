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
    int saveShopCar(ShopCarCondition shopCar);

    /**
     * 查询购物车
     * @author: wangbaokuo
     * @date: 2018/8/3 14:05
     * @param: condition
     * @return: List<ShopCarVO>
     */
    List<ShopCarVO> findShopCar(ShopCarCondition condition);

    /**
     * 校验商品上下架
     * @author: wangbaokuo
     * @date: 2018/8/3 16:06
     * @param: skuCode
     * @return: Boolean
     */
    Boolean checkShelves(String skuCode);

    /**
     * 删除购物车
     * @author: wangbaokuo
     * @date: 2018/8/3 16:20
     * @param: condition
     * @return: int
     */
    int removeShopCar(ShopCarCondition condition);
}
