package com.winhxd.b2c.order.dao;

import com.winhxd.b2c.common.domain.shopcar.model.ShopCar;

import java.util.List;

public interface ShopCarMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopCar record);

    int insertSelective(ShopCar record);

    ShopCar selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopCar record);

    int updateByPrimaryKey(ShopCar record);

    /**
     * @auther: wangbaokuo
     * @date: 2018/8/3 13:33
     * @deprecated: 根据条件查询ShopCar
     * @param: condition
     * @return: List<ShopCar>
     */
    List<ShopCar> selectShopCars(ShopCar shopCar);

    /**
     * @auther: wangbaokuo
     * @date: 2018/8/3 13:35
     * @deprecated: 根据条件删除ShopCar
     * @param: condition
     * @return: int
     */
    int deleteShopCars(ShopCar shopCar);
}