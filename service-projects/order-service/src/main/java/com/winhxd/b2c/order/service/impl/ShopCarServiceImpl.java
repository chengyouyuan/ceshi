package com.winhxd.b2c.order.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.order.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.order.model.ShopCar;
import com.winhxd.b2c.common.domain.order.vo.ShopCarVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.order.dao.ShopCarMapper;
import com.winhxd.b2c.order.service.ShopCarService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author: wangbaokuo
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
        if (!checkShelves(condition.getSkuCode())) {
            logger.error("商品加购异常{}  商品下架");
            throw new BusinessException(BusinessCode.CODE_402010);
        }
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
        List<ShopCarVO> resultList = new ArrayList<>(shopCars.size());
        if (CollectionUtils.isNotEmpty(shopCars)) {
            ShopCarVO vo;
            for(Iterator it = shopCars.iterator(); it.hasNext();){
                vo = new ShopCarVO();
                BeanUtils.copyProperties(it.next(), vo);
                // TODO 需要获取商品名称,图片,商品单价等信息返回给用户
                vo.setProdName(null);
                vo.setProdImg(null);
                vo.setProdPrice(null);
                vo.setProdStatus(checkShelves(vo.getSkuCode()) ? StoreProductStatusEnum.PUTAWAY.getStatusCode() : StoreProductStatusEnum.UNPUTAWAY.getStatusCode());
                resultList.add(vo);
            }
        }
        return resultList;
    }

    /**
     * 校验商品上下架
     * @author: wangbaokuo
     * @date: 2018/8/3 15:27
     * @param: [skuCode]
     * @return: java.lang.Boolean
     */
    @Override
    public Boolean checkShelves(String skuCode){
        Boolean flag = false;
        // TODO 校验上下架
        return flag;
    }

    @Override
    public int removeShopCar(ShopCarCondition condition) {
        ShopCar shopCar = new ShopCar();
        // TODO TOCKEN获取当前用户信息
        Long customerId = null;
        shopCar.setCustomerId(customerId);
        shopCar.setStoreId(condition.getStoreId());
        return shopCarMapper.deleteShopCars(shopCar);
    }
}
