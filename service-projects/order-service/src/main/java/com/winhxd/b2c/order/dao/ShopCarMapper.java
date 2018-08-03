package com.winhxd.b2c.order.dao;

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
    int deleteShopCars(ShopCar shopCar);
}