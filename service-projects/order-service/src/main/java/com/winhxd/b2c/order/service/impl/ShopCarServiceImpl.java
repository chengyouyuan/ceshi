package com.winhxd.b2c.order.service.impl;

import com.winhxd.b2c.common.domain.shopcar.model.ShopCar;
import com.winhxd.b2c.order.dao.ShopCarMapper;
import com.winhxd.b2c.order.service.ShopCarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @auther: wangbaokuo
 * @date: 2018/8/2 20:20
 * @description:
 */
@Service
public class ShopCarServiceImpl implements ShopCarService {

    private static final Logger logger = LoggerFactory.getLogger(ShopCarServiceImpl.class);

    private ShopCarMapper shopCarMapper;

    @Override
    public int saveShopCar(ShopCar shopCar) {
        return 0;
    }
}
