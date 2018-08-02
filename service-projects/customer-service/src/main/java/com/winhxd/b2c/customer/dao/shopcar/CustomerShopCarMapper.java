package com.winhxd.b2c.customer.dao.shopcar;

import com.winhxd.b2c.common.domain.shopcar.model.CustomerShopCar;

public interface CustomerShopCarMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerShopCar record);

    int insertSelective(CustomerShopCar record);

    CustomerShopCar selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerShopCar record);

    int updateByPrimaryKey(CustomerShopCar record);
}