package com.winhxd.b2c.customer.service.impl;

import com.winhxd.b2c.common.domain.shopcar.model.CustomerShopCar;
import com.winhxd.b2c.customer.dao.shopcar.CustomerShopCarMapper;
import com.winhxd.b2c.customer.service.CustomerShopCarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @auther: wangbaokuo
 * @date: 2018/8/2 20:20
 * @description:
 */
@Service
public class CustomerShopCarServiceImpl implements CustomerShopCarService{

    private static final Logger logger = LoggerFactory.getLogger(CustomerShopCarServiceImpl.class);

    private CustomerShopCarMapper customerShopCarMapper;

    @Override
    public int saveCustomerShopCar(CustomerShopCar customerShopCar) {
        return customerShopCarMapper.insertSelective(customerShopCar);
    }
}
