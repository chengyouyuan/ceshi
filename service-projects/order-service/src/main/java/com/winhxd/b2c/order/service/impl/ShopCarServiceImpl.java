package com.winhxd.b2c.order.service.impl;

import com.winhxd.b2c.common.domain.shopcar.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.shopcar.model.ShopCar;
import com.winhxd.b2c.common.domain.shopcar.vo.ShopCarVO;
import com.winhxd.b2c.order.dao.ShopCarMapper;
import com.winhxd.b2c.order.service.ShopCarService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @auther: wangbaokuo
 * @date: 2018/8/2 20:20
 * @description:
 */
@Service
public class ShopCarServiceImpl implements ShopCarService {

    private static final Logger logger = LoggerFactory.getLogger(ShopCarServiceImpl.class);

    @Resource
    private ShopCarMapper shopCarMapper;

    @Transactional(rollbackFor= {Exception.class})
    @Override
    public int saveShopCar(ShopCarCondition condition){
        // 加购：1、存在，则删除再保存，2、不存在，直接保存
        ShopCar shopCar = new ShopCar();
        // TODO TOCKEN获取当前用户信息
        Long customerId = null;
        shopCar.setCustomerId(customerId);
        shopCar.setStoreId(condition.getStoreId());
        List<ShopCar> shopCars = shopCarMapper.selectShopCars(shopCar);
        if (CollectionUtils.isNotEmpty(shopCars)) {
            shopCarMapper.deleteShopCars(shopCar);
        }
        Date current = new Date();
        shopCar.setSkuCode(condition.getSkuCode());
        shopCar.setProdNum(condition.getProdNum());
        shopCar.setCreated(current);
        shopCar.setCreatedby(customerId);
        shopCar.setUpdated(current);
        shopCar.setUpdatedby(customerId);
        return shopCarMapper.insertSelective(shopCar);
    }

    @Override
    public List<ShopCarVO> findShopCar(ShopCarCondition condition) {
        ShopCar shopCar = new ShopCar();
        // TODO TOCKEN获取当前用户信息
        Long customerId = null;
        shopCar.setCustomerId(customerId);
        shopCar.setStoreId(condition.getStoreId());
        List<ShopCar> shopCars = shopCarMapper.selectShopCars(shopCar);
        if (CollectionUtils.isNotEmpty(shopCars)) {
//            List<ShopCarVO> resultList = new ArrayList<>(shopCar.size());
            ShopCarVO vo;
            for(Iterator it = shopCars.iterator(); it.hasNext();){
                vo = new ShopCarVO();
                it.next();
            }
        }
        return null;
    }


}
