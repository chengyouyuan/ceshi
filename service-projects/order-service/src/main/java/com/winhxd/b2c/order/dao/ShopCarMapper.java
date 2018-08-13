package com.winhxd.b2c.order.dao;

import com.winhxd.b2c.common.domain.order.condition.ShopCartProductCondition;
import com.winhxd.b2c.common.domain.order.model.ShopCar;

import java.util.List;

public interface ShopCarMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopCar record);

    int insertSelective(ShopCar record);

    ShopCar selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopCar record);

    int updateByPrimaryKey(ShopCar record);

    /**
     * 根据条件查询ShopCar
     * @author: wangbaokuo
     * @date: 2018/8/3 13:33
     * @param: shopCar
     * @return: List<ShopCar>
     */
    List<ShopCar> selectShopCars(ShopCar shopCar);

    /**
     * 根据条件删除ShopCar
     * @author: wangbaokuo
     * @date: 2018/8/3 13:35
     * @param: shopCar
     * @return: int
     */
    int deleteShopCars(Long customerId);

    /**
     * 根据skuCode查用户购物车
     * @author: wangbaokuo
     * @date: 2018/8/10 11:11
     * @param:
     * @return: ShopCar
     */
    ShopCar selectShopCarsBySkuCode(ShopCar shopCar);

    /**
     * 删除用户门店下购物车
     * @author: wangbaokuo
     * @date: 2018/8/10 11:15
     * @param:
     * @return:
     */
    int deleteShopCarsByStoreId(ShopCar shopCar);

    /**
     * 根据门店 用户 sku集合 获取集合信息
     * @param condition
     * @return
     */
    List<ShopCar> queryShopCartBySelective(ShopCartProductCondition condition);
}