package com.winhxd.b2c.customer.service;

import com.winhxd.b2c.common.domain.shopcar.model.ShopCar;
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
    int saveShopCar(ShopCar shopCar);
}
